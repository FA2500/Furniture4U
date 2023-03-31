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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;

public class AddAddressActivity extends AppCompatActivity {

    EditText name,address,city,postCode,phoneNumber;
    Toolbar toolbar;
    Button addAddressBtn;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
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
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);
        addAddressBtn = findViewById(R.id.ad_add_address);

        //onclick
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

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
                else if(userCity.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "City is empty", Toast.LENGTH_SHORT).show();
                }
                else if(userCode.isEmpty())
                {
                    Toast.makeText(AddAddressActivity.this, "Code is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!username.isEmpty() && !userNumber.isEmpty() && !userAddress.isEmpty() && !userCity.isEmpty() && !userCode.isEmpty())
                {
                    Map<String,String> map = new HashMap<>();
                    map.put("name",username);
                    map.put("phone",userNumber);
                    map.put("address",userAddress);
                    map.put("city",userCity);
                    map.put("code",userCode);

                    firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/address")
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
                            });
                }
            }
        });
    }
}