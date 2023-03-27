package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.com.HomePageActivity;

public class EmailLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                db.collection("user").document(user.getUid())
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
                                        });


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