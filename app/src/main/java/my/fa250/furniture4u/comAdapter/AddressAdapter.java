package my.fa250.furniture4u.comAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.AddressModel;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressModel> list;
    SelectedAddress selectedAddress;

    private RadioButton selectedRadioBtn;

    public AddressAdapter(Context context, List<AddressModel> list, SelectedAddress selectedAddress) {
        this.context = context;
        this.list = list;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhone());
        holder.address.setText(list.get(position).getAddress());
        if(list.get(position).getisPrimary())
        {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText("Primary");
        }


        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(AddressModel address:list)
                {
                    address.setisPrimary(false);
                }
                list.get(holder.getAdapterPosition()).setisPrimary(true);

                if(selectedRadioBtn!=null)
                {
                    selectedRadioBtn.setChecked(false);
                }
                selectedRadioBtn = (RadioButton) v;
                selectedRadioBtn.setChecked(true);
                selectedAddress.setAddress(list.get(holder.getAdapterPosition()).getAddress());
                selectedAddress.setKeyAddress(list.get(holder.getAdapterPosition()).getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone,address,status;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.addNameX);
            address = itemView.findViewById(R.id.address_add);
            phone = itemView.findViewById(R.id.addPhoneX);
            status = itemView.findViewById(R.id.addStatusX);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }

    public interface SelectedAddress
    {
        void setAddress(String address);
        void setKeyAddress(String address);
    }
}
