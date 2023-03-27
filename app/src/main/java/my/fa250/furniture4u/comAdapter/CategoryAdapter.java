package my.fa250.furniture4u.comAdapter;

import android.content.Context;
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
import my.fa250.furniture4u.model.CategoryModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final Context context;
    private final List<CategoryModel> list;

    public CategoryAdapter(Context context, List<CategoryModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Log.d("Cat", String.valueOf(holder.catTV.getId()));
        Glide.with(context)
                .load(list.get(position).getImg_url())
                .into(holder.catImg);
        holder.catTV.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catImg;
        TextView catTV;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            catImg = itemView.findViewById(R.id.cat_img);
            catTV = itemView.findViewById(R.id.cat_name);
        }
    }
}
