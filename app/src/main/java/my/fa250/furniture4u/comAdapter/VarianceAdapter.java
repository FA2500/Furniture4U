package my.fa250.furniture4u.comAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == holder.btn.getId())
                {
                    holder.btn.setBackgroundColor(ContextCompat.getColor(context, R.color.medium_blue));
                    holder.btn.setTextColor(Color.WHITE);
                    Intent intent = new Intent("VarianceButtonClick");
                    intent.putExtra("itemName",holder.btn.getText().toString());
                    intent.putExtra("itemPrice",list.get(holder.getAdapterPosition()).getPrice());
                    intent.putExtra("btnId",holder.btn.getId());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                else
                {
                    holder.btn.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                    holder.btn.setTextColor(Color.BLACK);
                }

            }
        });
        LocalBroadcastManager.getInstance(context).registerReceiver(holder.MessageReceiver, new IntentFilter("VarianceButtonClick2"));
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

        public BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String btnName = intent.getStringExtra("btnName");
                Log.d("VAR ADAPTER","RECEIVED item = "+btnName);
                if((!btn.getText().toString().equals(btnName)))
                {
                    btn.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                    btn.setTextColor(Color.BLACK);
                }
                /*else
                {


                }*/
            }
        };
    }
}
