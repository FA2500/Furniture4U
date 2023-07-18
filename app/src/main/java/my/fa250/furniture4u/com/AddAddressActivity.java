package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;

public class AddAddressActivity extends AppCompatActivity {

    EditText name,phoneNumber,address,postCode,district,state;
    Toolbar toolbar;
    Button addAddressBtn;

    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initUI();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.ad_name);
        phoneNumber = findViewById(R.id.ad_district);
        address = findViewById(R.id.ad_address);
        postCode = findViewById(R.id.ad_code);
        district = findViewById(R.id.ad_district);
        state = findViewById(R.id.ad_state);

        addAddressBtn = findViewById(R.id.ad_add_address);

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
                    Toast.makeText(AddAddressActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userNumber.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "Phone Number is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userAddress.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "Address is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userCode.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "Code is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userDist.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "District is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userState.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "State is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!username.isEmpty() && !userNumber.isEmpty() && !userAddress.isEmpty() && !userCode.isEmpty() && !userDist.isEmpty() && !userState.isEmpty())
                {
                    Map<String,String> map = new HashMap<>();
                    map.put("name",username);
                    map.put("phone",userNumber);
                    map.put("address",userAddress);
                    map.put("code",userCode);
                    map.put("district",userDist);
                    map.put("state",userState);

                    database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/address")
                            .push()
                            .setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(AddAddressActivity.this, "Address successfully saved", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddAddressActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    /*firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/address")
                            .add(map)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(AddAddressActivity.this, "Address successfully saved", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(AddAddressActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                }
            }
        });
    }
}