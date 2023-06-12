package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.VarianceModel;

public class VarianceAdapter extends RecyclerView.Adapter<VarianceAdapter.Viewholder>{

    private Context context;
    private List<VarianceModel> list;

    public VarianceAdapter(Context context, List<VarianceModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VarianceAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.variance_btn,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VarianceAdapter.Viewholder holder, int position) {
        holder.btn.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        Button btn;
        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            btn = itemView.findViewById(R.id.varModelBtn);
        }
    }
}
