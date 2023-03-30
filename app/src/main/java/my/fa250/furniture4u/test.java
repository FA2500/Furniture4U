package my.fa250.furniture4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import my.fa250.furniture4u.ar.checkARSupported;
import my.fa250.furniture4u.auth.MultipleSignInActivity;
import my.fa250.furniture4u.com.CartActivity;


public class test extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);*/
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_logout)
        {
            mAuth.signOut();
            Intent intent = new Intent(test.this, MultipleSignInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(test.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }*/
}