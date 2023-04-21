package my.fa250.furniture4u.comAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.PopModel;

public class PopAdapter extends RecyclerView.Adapter<PopAdapter.Viewholder> {

    private Context context;
    private List<PopModel> popModelList;

    public PopAdapter(Context context, List<PopModel> popModelList) {
        this.context = context;
        this.popModelList = popModelList;
    }

    @NonNull
    @Override
    public PopAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_items,parent,false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull PopAdapter.Viewholder holder, int position) {
        Glide.with(context).load(popModelList.get(position).getImg_url()).into(holder.popImg);
        holder.popTv.setText(popModelList.get(position).getName());
        holder.popPrice.setText(String.format("%.2f", popModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("details",popModelList.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return popModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView popImg;
        TextView popTv,popPrice;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            popImg = itemView.findViewById(R.id.all_img);
            popTv = itemView.findViewById(R.id.all_product_name);
            popPrice = itemView.findViewById(R.id.all_price);
        }
    }
}
