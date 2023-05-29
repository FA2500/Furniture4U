package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.AddressAdapter;
import my.fa250.furniture4u.model.AddressModel;

public class AddressActivity extends AppCompatActivity implements Serializable, AddressAdapter.SelectedAddress {

    Button addAddress;
    RecyclerView recyclerView;

    //adapter
    List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;

    //Firebase
    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //UI
    Button paymentBtn;
    Toolbar toolbar;

    String mAddress = " ";
    private String addressKey = "";

    private Double total;
    private List<String> cardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Intent intent = getIntent();
        total = intent.getDoubleExtra("total", 0.0);
        cardID = (List<String>) intent.getSerializableExtra("cardID");


        initUI();
        getData();
    }

    private void initUI()
    {
        //toolbar
        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addAddress = findViewById(R.id.add_address_btn);
        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAddress.isEmpty() && !addressKey.isEmpty())
                {
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    intent.putExtra("total",total);
                    intent.putExtra("cardID", (Serializable) cardID);
                    intent.putExtra("address",mAddress);
                    intent.putExtra("addressID",addressKey);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AddressActivity.this, "Please Choose your address first", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData()
    {
        database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot addressSnapshot : snapshot.getChildren())
                {
                    AddressModel addressModel = addressSnapshot.getValue(AddressModel.class);
                    addressModel.setID(addressSnapshot.getKey());
                    addressModelList.add(addressModel);
                    addressAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Error reading data", error.toException());
            }
        });
        /*firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot doc : task.getResult())
                            {
                                AddressModel addressModel = doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });*/
    }

    @Override
    public void setAddress(String address) {
        mAddress = address;
    }

    @Override
    public void setKeyAddress(String address) {
        addressKey = address;
    }
}