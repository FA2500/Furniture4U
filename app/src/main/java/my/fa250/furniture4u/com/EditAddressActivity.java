package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.AddressModel;

public class EditAddressActivity extends AppCompatActivity {

    EditText name,phoneNumber,address,postCode,district,state;
    Toolbar toolbar;
    Button addAddressBtn;

    CheckBox setAsPrimary;

    private Double total;

    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        initUI();
        total = getIntent().getDoubleExtra("total", 0.0);
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.edit_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAddressActivity.this, AddressActivity.class);
                intent.putExtra("total",total);
                startActivity(intent);
            }
        });

        name = findViewById(R.id.ed_name);
        phoneNumber = findViewById(R.id.ed_phone);
        address = findViewById(R.id.ed_address);
        postCode = findViewById(R.id.ed_code);
        district = findViewById(R.id.ed_district);
        state = findViewById(R.id.ed_state);

        name.setText(getIntent().getStringExtra("name"));
        phoneNumber.setText(getIntent().getStringExtra("phone"));
        address.setText(getIntent().getStringExtra("address"));
        postCode.setText(getIntent().getStringExtra("postcode"));
        district.setText(getIntent().getStringExtra("district"));
        state.setText(getIntent().getStringExtra("state"));
        ID = getIntent().getStringExtra("ID");

        setAsPrimary = findViewById(R.id.EdprimaryAdd);

        setAsPrimary.setChecked(getIntent().getBooleanExtra("isPrimary",false));

        addAddressBtn = findViewById(R.id.addressEditBtn);

        //onclick
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String userNumber = phoneNumber.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postCode.getText().toString();
                String userDist = district.getText().toString();
                String userState = state.getText().toString();

                String final_address = "";

                if(username.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userNumber.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "Phone Number is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userAddress.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "Address is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userCode.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "Code is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userDist.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "District is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userState.isEmpty())
                {
                    Toast.makeText(EditAddressActivity.this, "State is empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",username);
                    map.put("phone",userNumber);
                    map.put("address",userAddress);
                    map.put("code",userCode);
                    map.put("district",userDist);
                    map.put("state",userState);
                    map.put("isPrimary",setAsPrimary.isChecked());

                    HashMap<String,Object> a = new HashMap<String, Object>() ;
                    a.put("isPrimary",false);

                    if(setAsPrimary.isChecked())
                    {
                        database.getReference("user/"+mAuth.getCurrentUser().getUid()+"/address").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(DataSnapshot dataSnapshot: task.getResult().getChildren())
                                    {
                                        if(!dataSnapshot.getKey().equals(ID))
                                        {
                                            database.getReference("user/"+mAuth.getCurrentUser().getUid()+"/address/"+dataSnapshot.getKey()).updateChildren(a);
                                        }

                                    }
                                }
                            }
                        });
                    }

                    database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/address").child(ID)
                            .updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(EditAddressActivity.this, "Address successfully saved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditAddressActivity.this, AddressActivity.class);
                                        intent.putExtra("total",total);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(EditAddressActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}