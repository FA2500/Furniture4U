package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.CartModel;
import my.fa250.furniture4u.model.PreparingModel;

public class PreparingAdapter extends RecyclerView.Adapter<PreparingAdapter.ViewHolder>{
    private Context context;
    private List<CartModel> list;

    private DecimalFormat DF = new DecimalFormat("0.00");

    public PreparingAdapter(Context context, List<CartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PreparingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreparingAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreparingAdapter.ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText("RM"+DF.format(list.get(position).getProductPrice()));
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
       // holder.totalQuan.setText(String.valueOf(list.get(position).getTotalQuantity()));

        Glide.with(context)
                .load(list.get(position).getImg_url().get(0))
                .into(holder.catImg);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("details",list.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView name,price,date,time,totalQuan,totalPrice;
        Button quan;
        CheckBox checkBox;

        ImageView catImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cart_card);
            catImg = itemView.findViewById(R.id.cartImg);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            totalPrice = itemView.findViewById(R.id.total_price_cart);
        }
    }
}
