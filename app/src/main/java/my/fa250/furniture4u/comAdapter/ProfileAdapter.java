package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.CartActivity;
import my.fa250.furniture4u.com.PreparingActivity;
import my.fa250.furniture4u.model.ProfileModel;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private final Context context;
    private final List<ProfileModel> list;

    public ProfileAdapter(Context context, List<ProfileModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_order_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(list.get(position).getSrcID());
        holder.title.setText(list.get(position).getTitle());

        if(list.get(position).getNoOfNotif() > 0)
        {
            holder.num.setText(String.valueOf(list.get(holder.getAdapterPosition()).getNoOfNotif()));
            holder.num.setVisibility(View.VISIBLE);
        }


        if(list.get(position).getTitle().equals("To Pay"))
        {
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CartActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        else if(list.get(position).getTitle().equals("Preparing"))
        {
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PreparingActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView num;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            icon = itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.profileTitle);
            num = itemView.findViewById(R.id.numberTV);
        }
    }
}
