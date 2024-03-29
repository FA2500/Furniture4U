package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
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
import my.fa250.furniture4u.com.SearchActivity;
import my.fa250.furniture4u.com.SearchResultActivity;
import my.fa250.furniture4u.com.ShowAllActivity;
import my.fa250.furniture4u.model.SearchModel;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Viewholder> {
    private Context context;
    private final List<SearchModel> list;

    public SearchAdapter(Context context, List<SearchModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.Viewholder holder, int position) {
        String a = list.get(position).getImg_url().get(0);
        Glide.with(context)
                .load(a)
                .into(holder.catImg);
        holder.catTV.setText(list.get(position).getName());
        holder.catTV.setTextColor(Color.BLACK);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchResultActivity.class);
                intent.putExtra("query",list.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView catImg;
        TextView catTV;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.imageView6);
            catTV = itemView.findViewById(R.id.textView14);
        }
    }
}
