package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.ProductDetailActivity;
import my.fa250.furniture4u.model.ShowAllModel;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.Viewholder> {

    private Context context;
    private List<ShowAllModel> list;

    DecimalFormat DF = new DecimalFormat(".00");

    public ShowAllAdapter(Context context, List<ShowAllModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShowAllAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllAdapter.Viewholder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.ItemImage);
        holder.aCost.setText("RM"+(DF.format(list.get(position).getPrice())));
        holder.aName.setText(list.get(position).getName());
        float a = (float) list.get(position).getRating();
        holder.ratingBar.setRating(a);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView ItemImage;
        TextView aCost,aName;
        RatingBar ratingBar;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ItemImage = itemView.findViewById(R.id.item_image);
            aCost = itemView.findViewById(R.id.item_cost);
            aName = itemView.findViewById(R.id.item_nam);
            ratingBar = itemView.findViewById(R.id.all_product_detail_rate);

        }
    }
}
