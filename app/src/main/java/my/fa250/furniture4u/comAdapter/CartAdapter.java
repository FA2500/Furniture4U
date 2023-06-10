package my.fa250.furniture4u.comAdapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.CartModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> list;
    private DecimalFormat DF = new DecimalFormat("0.00");

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
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText("RM"+DF.format(list.get(position).getProductPrice()));
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        //holder.totalQuan.setText(String.valueOf(list.get(position).getTotalQuantity()));
        holder.quan.setText(String.valueOf(list.get(position).getTotalQuantity()));

        Glide.with(context)
                .load(list.get(position).getImg_url())
                .into(holder.catImg);

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
                if(holder.checkBox.isChecked())
                {
                    holder.checkBox.setText("Selected");
                    Intent intent = new Intent("CartTotalAmount");
                    intent.putExtra("totalAmount",list.get(holder.getAdapterPosition()).getTotalPrice());
                    intent.putExtra("status", "add");
                    intent.putExtra("cartID", list.get(holder.getAdapterPosition()).getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("INTENT","SENDING VALUE "+list.get(holder.getAdapterPosition()).getTotalPrice());
                }
                else
                {
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
        TextView name,price,date,time,totalQuan,totalPrice;
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
