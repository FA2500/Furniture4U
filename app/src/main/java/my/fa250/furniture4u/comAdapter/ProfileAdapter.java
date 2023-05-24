package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import my.fa250.furniture4u.R;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView note;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            icon = itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.profileTitle);
            note = itemView.findViewById(R.id.notificationTextView);
        }
    }
}
