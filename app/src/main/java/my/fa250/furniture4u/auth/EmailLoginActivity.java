package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.com.HomePageActivity;

public class EmailLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    //UI
    private EditText emailET;
    private EditText passET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        mAuth = FirebaseAuth.getInstance();

        initUI();
    }

    //public method

    public void BtnLoginEmail(View v)
    {
        mAuth.signInWithEmailAndPassword(emailET.getText().toString(),passET.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                user = mAuth.getCurrentUser();
                                database.getReference("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            UserInfo.setName(snapshot.child("name").getValue(String.class));
                                            UserInfo.setEmail(snapshot.child("email").getValue(String.class));
                                            UserInfo.setPhone(snapshot.child("phone").getValue(String.class));
                                            UserInfo.setProvider(snapshot.child("provider").getValue(String.class));
                                            UserInfo.setRole("Customer");
                                            Log.d("Database", "Data successfully retrieved!");
                                            Intent intent = new Intent(EmailLoginActivity.this, HomePageActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(EmailLoginActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(EmailLoginActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                /*db.collection("user").document(user.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot doc = task.getResult();
                                                UserInfo.setName(doc.get("name").toString());
                                                UserInfo.setEmail(doc.get("email").toString());
                                                UserInfo.setPhone(doc.get("phone").toString());
                                                UserInfo.setRole("Customer");
                                                Log.d("Database", "DocumentSnapshot successfully written!");
                                                Intent intent = new Intent(EmailLoginActivity.this, HomePageActivity.class);
                                                startActivity(intent);
                                            }
                                        });*/


                            }
                        });


    }

    public void BtnEmailBack(View v)
    {
        Intent intent = new Intent(this, MultipleSignInActivity.class);
        startActivity(intent);
    }

    public void BtnEmailRegister(View v)
    {
        Intent intent = new Intent(this, EmailRegisterActivity.class);
        startActivity(intent);
    }

    //private

    private void initUI()
    {
        emailET = findViewById(R.id.editTextTextEmailAddress);
        passET = findViewById(R.id.editTextTextPassword);
    }


}