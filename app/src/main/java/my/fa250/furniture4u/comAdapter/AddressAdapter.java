package my.fa250.furniture4u.comAdapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.AddressActivity;
import my.fa250.furniture4u.com.CartActivity;
import my.fa250.furniture4u.com.EditAddressActivity;
import my.fa250.furniture4u.model.AddressModel;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressModel> list;
    SelectedAddress selectedAddress;

    private RadioButton selectedRadioBtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAddressActivity.class);
                intent.putExtra("ID",list.get(holder.getAdapterPosition()).getID());
                intent.putExtra("name",list.get(holder.getAdapterPosition()).getName());
                intent.putExtra("phone",list.get(holder.getAdapterPosition()).getPhone());
                intent.putExtra("address",list.get(holder.getAdapterPosition()).getAddress());
                intent.putExtra("postcode",list.get(holder.getAdapterPosition()).getCode());
                intent.putExtra("district",list.get(holder.getAdapterPosition()).getDistrict());
                intent.putExtra("state",list.get(holder.getAdapterPosition()).getState());
                intent.putExtra("isPrimary",list.get(holder.getAdapterPosition()).getisPrimary());
                intent.putExtra("total",list.get(holder.getAdapterPosition()).getTotal());
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to delete this address?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference("user").child(mAuth.getUid()).child("address").child(list.get(holder.getAdapterPosition()).getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(context, AddressActivity.class);
                                startActivity(context,intent,null);
                            }
                        });
                        // Delete the item
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone,address,status;

        Button edit,remove;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.addNameX);
            address = itemView.findViewById(R.id.address_add);
            phone = itemView.findViewById(R.id.addPhoneX);
            status = itemView.findViewById(R.id.addStatusX);
            edit = itemView.findViewById(R.id.addEditBtn);
            remove = itemView.findViewById(R.id.addRemoveBtn);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }

    public interface SelectedAddress
    {
        void setAddress(String address);
        void setKeyAddress(String address);
    }
}
