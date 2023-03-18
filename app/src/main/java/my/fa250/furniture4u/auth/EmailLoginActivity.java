package my.fa250.furniture4u.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.furniture4u.R;
import com.google.firebase.auth.FirebaseAuth;

public class EmailLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        mAuth = FirebaseAuth.getInstance();
    }

    //public method

    public void BtnLoginEmail(View v)
    {

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


}