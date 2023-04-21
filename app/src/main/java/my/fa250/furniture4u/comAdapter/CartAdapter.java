package my.fa250.furniture4u.comAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.CartModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> list;

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
        holder.price.setText("RM"+list.get(position).getProductPrice());
        holder.date.setText(list.get(position).getCurrentDate());
        holder.time.setText(list.get(position).getCurrentTime());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.totalQuan.setText(String.valueOf(list.get(position).getTotalQuantity()));

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
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("INTENT","SENDING VALUE "+list.get(holder.getAdapterPosition()).getTotalPrice());
                }
                else
                {
                    holder.checkBox.setText("Not selected");
                    Intent intent = new Intent("CartTotalAmount");
                    intent.putExtra("totalAmount",-(list.get(holder.getAdapterPosition()).getTotalPrice()));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("INTENT","SENDING VALUE "+-(list.get(holder.getAdapterPosition()).getTotalPrice()));
                }
            }
        });


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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cart_card);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setText("Not selected");
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            totalPrice = itemView.findViewById(R.id.total_price_cart);
            totalQuan = itemView.findViewById(R.id.total_quantity_cart);
        }
    }
}
