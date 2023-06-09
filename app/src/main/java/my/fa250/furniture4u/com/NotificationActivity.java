package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import my.fa250.furniture4u.R;

public class NotificationActivity extends AppCompatActivity {

    //UI
    Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notifications");
        try {
            getSupportActionBar().setTitle("Notifications");
        }
        catch (Exception e)
        {
            Log.w("Action",e);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    // Handle home item click
                    Intent intent = new Intent(NotificationActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.action_search:
                    // Handle search item click
                    return true;
                case R.id.action_notifications:
                    // Handle notifications item click
                    return true;
                case R.id.action_profile:
                    // Handle profile item click
                    Intent intent2 = new Intent(NotificationActivity.this, ProfileActivity.class);
                    startActivity(intent2);
                    finish();
                    return true;
            }
            return false;
        });
    }

    private void getData()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_chat)
        {
            //mAuth.signOut();
            Intent intent = new Intent(NotificationActivity.this, ChatActivity.class);
            startActivity(intent);
            // finish();
        }
        else if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(NotificationActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }
}