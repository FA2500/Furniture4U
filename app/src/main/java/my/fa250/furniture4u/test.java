package my.fa250.furniture4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import my.fa250.furniture4u.ar.checkARSupported;
import my.fa250.furniture4u.auth.MultipleSignInActivity;



public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void goToAR(View v)
    {
        Intent intent = new Intent(test.this , checkARSupported.class);
        startActivity(intent);
    }

    public void goToECom(View v)
    {
        Intent intent = new Intent(test.this , MultipleSignInActivity.class);
        startActivity(intent);
    }

    public void goToML(View v)
    {
       // Intent intent = new Intent(test.this, MlActivity.class);
        //startActivity(intent);
    }
}