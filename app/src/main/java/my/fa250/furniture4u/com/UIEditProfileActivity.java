package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.auth.EmailRegisterActivity;

public class UIEditProfileActivity extends AppCompatActivity {

    EditText name,phone,email;
    Button submit;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiedit_profile);

        initUI();
        getData();
        showDialog();
    }

    private void initUI()
    {
        name = findViewById(R.id.PUName);
        phone = findViewById(R.id.PUPhone);
        email = findViewById(R.id.PUEmail);
        submit = findViewById(R.id.PUSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfile();
            }
        });
    }

    private void getData()
    {
        name.setText(UserInfo.getName());
        email.setText(UserInfo.getEmail());
        phone.setText(UserInfo.getPhone());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome");
        builder.setMessage("Thank you for registering, please update your information so that you can use this application");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void submitProfile()
    {
        if(name.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Name Field is empty", Toast.LENGTH_SHORT).show();
        }
        else if(email.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Email Field is empty", Toast.LENGTH_SHORT).show();
        }
        else if(phone.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Phone Field is empty", Toast.LENGTH_SHORT).show();
        }
        else if(phone.getText().toString().length() != 10 && phone.getText().toString().length() != 11)
        {
            Toast.makeText(this, "Phone Field must have 10 or 11 digits only", Toast.LENGTH_SHORT).show();
        }
        else if(phone.getText().toString().charAt(0) != '0')
        {
            Toast.makeText(this, "Phone Field must start with 0", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String,Object> a = new HashMap<>();
            a.put("email",email.getText().toString());
            a.put("name",name.getText().toString());
            a.put("phone",phone.getText().toString());
            UserInfo.setName(name.getText().toString());
            UserInfo.setEmail(email.getText().toString());
            UserInfo.setPhone(phone.getText().toString());
            database.getReference("user").child(mAuth.getUid()).updateChildren(a)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Database", "Data successfully written!");
                            Intent intent = new Intent(UIEditProfileActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Database", "Error writing data", e);
                        }
                    });
        }

    }
}