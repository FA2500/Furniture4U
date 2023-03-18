package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.furniture4u.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.com.HomePageActivity;

public class EmailRegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    private EditText ETName;
    private EditText ETEmail;
    private EditText ETPhone;
    private EditText ETPass;
    private EditText ETCPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
        mAuth = FirebaseAuth.getInstance();
        initializeUI();
    }

    private void initializeUI()
    {
        ETName = findViewById(R.id.ETRegisterName);
        ETEmail = findViewById(R.id.ETRegisterEmail);
        ETPhone = findViewById(R.id.ETRegisterPhone);
        ETPass = findViewById(R.id.ETRegisterPass);
        ETCPass = findViewById(R.id.ETRegisterCPass);
    }

    public void BtnRegisterEmail(View v)
    {
        //Validation
        if(ETName.getText().length() <= 0)
        {
            Toast.makeText(this, "Display Name is empty", Toast.LENGTH_SHORT).show();
        }
        else if(ETEmail.getText().length() <= 0)
        {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
        }
        else if(ETPhone.getText().length() <= 0)
        {
            Toast.makeText(this, "Phone is empty", Toast.LENGTH_SHORT).show();
        }
        else if(ETPass.getText().length() <= 0)
        {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
        }
        else if(ETCPass.getText().length() <= 0)
        {
            Toast.makeText(this, "Confirm Password is empty", Toast.LENGTH_SHORT).show();
        }
        else if(!(ETPass.getText().toString().equals(ETCPass.getText().toString())))
        {
            Toast.makeText(this, "Password is not the same as Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            createEmailAccount();

        }
    }

    public void BtnRegisterBack(View v)
    {
        Intent intent = new Intent(this, MultipleSignInActivity.class);
        startActivity(intent);
    }

    public void BtnLoginBack(View v)
    {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    //private method

    private void createEmailAccount()
    {
        mAuth.createUserWithEmailAndPassword(ETEmail.getText().toString(), ETCPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register ", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            RegisterSaveUserInfo(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailRegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //Log.d("Register ", ETEmail.getText().toString());
        //Log.d("Register ", ETCPass.getText().toString());
    }

    private void RegisterSaveUserInfo(FirebaseUser user)
    {
        UserInfo.setName(ETName.getText().toString());
        UserInfo.setEmail(ETEmail.getText().toString());
        UserInfo.setPhone(ETPhone.getText().toString());
        UserInfo.setRole("Customer");

        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("name", ETName.getText().toString());
        userinfo.put("email", ETEmail.getText().toString());
        userinfo.put("phone",ETPhone.getText().toString());
        userinfo.put("role", "Customer");

        db.collection("user").document(user.getUid())
                .set(userinfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Database", "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(EmailRegisterActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database", "Error writing document", e);
                    }
                });

    }
}