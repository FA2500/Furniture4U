package my.fa250.furniture4u.comAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.ProductModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> list;

    public ProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_product,parent,false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.prodImg);
        holder.prodText.setText(list.get(position).getName());
        holder.prodPrice.setText(String.format("%.2f", list.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImg;
        TextView prodText, prodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImg = itemView.findViewById(R.id.new_img);
            prodText = itemView.findViewById(R.id.new_product_name);
            prodPrice = itemView.findViewById(R.id.new_price);
        }
    }
}
