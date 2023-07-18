package my.fa250.furniture4u.comAdapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.CartActivity;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.CartModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> list;
    private DecimalFormat DF = new DecimalFormat("0.00");

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public CartAdapter(Context context, List<CartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        list.get(position).setIsInCart(false);
        list.get(holder.getAdapterPosition()).setIsInCart(false);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText("RM"+DF.format(list.get(position).getProductPrice()));
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        //holder.totalQuan.setText(String.valueOf(list.get(position).getTotalQuantity()));
        holder.quan.setText(String.valueOf(list.get(position).getTotalQuantity()));
        if(list.get(position).getVariance().size() > 1)
        {
            holder.provar.setVisibility(View.VISIBLE);
            holder.provar.setText("Variance = "+list.get(position).getColour());
            int varL = 0;
            for(int i = 0 ; i<list.get(position).getVariance().size();i++)
            {
                if(Objects.equals(list.get(position).getVariance().get(i), list.get(position).getColour()))
                {
                    varL = i;
                    break;
                }
            }
            varL*=3;
            Glide.with(context)
                    .load(list.get(position).getImg_url().get(varL))
                    .into(holder.catImg);
        }
        else
        {
            Glide.with(context)
                    .load(list.get(position).getImg_url().get(0))
                    .into(holder.catImg);
        }



        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("details",list.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked() && !list.get(holder.getAdapterPosition()).getIsInCart())
                {
                    list.get(holder.getAdapterPosition()).setIsInCart(true);
                    holder.checkBox.setText("Selected");
                    Intent intent = new Intent("CartTotalAmount");
                    intent.putExtra("totalAmount",list.get(holder.getAdapterPosition()).getTotalPrice());
                    intent.putExtra("status", "add");
                    intent.putExtra("cartID", list.get(holder.getAdapterPosition()).getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("INTENT","SENDING VALUE "+list.get(holder.getAdapterPosition()).getTotalPrice());
                }
                else if(!holder.checkBox.isChecked())
                {
                    list.get(holder.getAdapterPosition()).setIsInCart(false);
                    holder.checkBox.setText("Not selected");
                    Intent intent = new Intent("CartTotalAmount");
                    intent.putExtra("totalAmount",-(list.get(holder.getAdapterPosition()).getTotalPrice()));
                    intent.putExtra("status", "remove");
                    intent.putExtra("cartID", list.get(holder.getAdapterPosition()).getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("INTENT","SENDING VALUE "+-(list.get(holder.getAdapterPosition()).getTotalPrice()));
                }
            }
        });
        holder.minBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(holder.getAdapterPosition()).getTotalQuantity() > 1)
                {
                    list.get(holder.getAdapterPosition()).setTotalQuantity((list.get(holder.getAdapterPosition()).getTotalQuantity()-1));
                    list.get(holder.getAdapterPosition()).setTotalPrice(list.get(holder.getAdapterPosition()).getProductPrice()*list.get(holder.getAdapterPosition()).getTotalQuantity());
                    holder.price.setText("RM"+DF.format((list.get(holder.getAdapterPosition()).getTotalPrice())));
                    holder.quan.setText(String.valueOf(list.get(holder.getAdapterPosition()).getTotalQuantity()));

                    if(holder.checkBox.isChecked())
                    {
                        Intent intent = new Intent("CartTotalAmount");
                        intent.putExtra("totalAmount",-(list.get(holder.getAdapterPosition()).getProductPrice()));
                        intent.putExtra("totalQuan",list.get(holder.getAdapterPosition()).getTotalQuantity());
                        intent.putExtra("cartID", list.get(holder.getAdapterPosition()).getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert");
                    builder.setMessage("Do you want to delete this item?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database.getReference("user").child(mAuth.getUid()).child("cart").child(list.get(holder.getAdapterPosition()).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(context, CartActivity.class);
                                    startActivity(context,intent,null);
                                }
                            });
                            // Delete the item
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }

            }
        });
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(holder.getAdapterPosition()).getTotalQuantity() >= 1)
                {
                    list.get(holder.getAdapterPosition()).setTotalQuantity((list.get(holder.getAdapterPosition()).getTotalQuantity()+1));
                    list.get(holder.getAdapterPosition()).setTotalPrice(list.get(holder.getAdapterPosition()).getProductPrice()*list.get(holder.getAdapterPosition()).getTotalQuantity());
                    holder.price.setText("RM"+DF.format((list.get(holder.getAdapterPosition()).getTotalPrice())));
                    holder.quan.setText(String.valueOf(list.get(holder.getAdapterPosition()).getTotalQuantity()));

                    if(holder.checkBox.isChecked())
                    {
                        Intent intent = new Intent("CartTotalAmount");
                        intent.putExtra("totalAmount",list.get(holder.getAdapterPosition()).getProductPrice());
                        intent.putExtra("totalQuan",list.get(holder.getAdapterPosition()).getTotalQuantity());
                        intent.putExtra("cartID", list.get(holder.getAdapterPosition()).getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
                else
                {

                }
            }
        });
        LocalBroadcastManager.getInstance(context).registerReceiver(holder.MessageReceiver, new IntentFilter("checkAll"));

        //Total
        /*totalAmount = totalAmount + list.get(position).getTotalPrice();
        Intent intent = new Intent("CartTotalAmount");
        intent.putExtra("totalAmount",totalAmount);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name,price,date,time,totalQuan,totalPrice,minBtn,addBtn,provar;
        CheckBox checkBox;
        Button quan;

        ImageView catImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cart_card);
            checkBox = itemView.findViewById(R.id.checkBox);
            price = itemView.findViewById(R.id.product_price);
            checkBox.setText("Not selected");
            quan = itemView.findViewById(R.id.product_quant);
            catImg = itemView.findViewById(R.id.cartImg);
            name = itemView.findViewById(R.id.product_name);
            //price = itemView.findViewById(R.id.product_price);
            totalPrice = itemView.findViewById(R.id.total_price_cart);
            //totalQuan = itemView.findViewById(R.id.total_quantity_cart);
            minBtn = itemView.findViewById(R.id.minusQuanBtn);
            addBtn = itemView.findViewById(R.id.addQuanBtn);
            provar = itemView.findViewById(R.id.product_var);
        }

        public BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean cartStat = intent.getBooleanExtra("status",false);
                checkBox.setChecked(cartStat);
                if(checkBox.isChecked())
                {
                    checkBox.setText("Selected");
                    Intent intent2 = new Intent("CartTotalAmount");
                    intent2.putExtra("totalAmount",list.get(getAdapterPosition()).getTotalPrice());
                    intent2.putExtra("status", "add");
                    intent2.putExtra("cartID", list.get(getAdapterPosition()).getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
                    Log.d("INTENT","SENDING VALUE "+list.get(getAdapterPosition()).getTotalPrice());
                }
                else
                {
                    checkBox.setText("Not selected");
                    Intent intent2 = new Intent("CartTotalAmount");
                    intent2.putExtra("totalAmount",-(list.get(getAdapterPosition()).getTotalPrice()));
                    intent2.putExtra("status", "remove");
                    intent2.putExtra("cartID", list.get(getAdapterPosition()).getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
                    Log.d("INTENT","SENDING VALUE "+-(list.get(getAdapterPosition()).getTotalPrice()));
                }
            }
        };

        public BroadcastReceiver MessageReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean cartStat = intent.getBooleanExtra("status",false);
                checkBox.setChecked(cartStat);
            }
        };
    }


}
