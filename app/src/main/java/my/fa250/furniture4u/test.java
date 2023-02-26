package my.fa250.furniture4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.furniture4u.R;

import my.fa250.furniture4u.ar.checkARSupported;

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
}