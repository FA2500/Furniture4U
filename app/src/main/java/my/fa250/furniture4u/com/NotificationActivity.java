package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.ArCoreApk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.AddressAdapter;
import my.fa250.furniture4u.comAdapter.NotificationAdapter;
import my.fa250.furniture4u.ml.ContextActivity;
import my.fa250.furniture4u.model.AddressModel;
import my.fa250.furniture4u.model.NotificationModel;

public class NotificationActivity extends AppCompatActivity {

    //UI
    Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

    RecyclerView recyclerView;
    List<NotificationModel> notificationModels;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
        getData();
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

        recyclerView = findViewById(R.id.notifRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationModels = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationModels);
        recyclerView.setAdapter(notificationAdapter);

    }

    private void getData()
    {
        database.getReference("user").child(mAuth.getUid()).child("notification").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    for(DataSnapshot notifSnapshot : task.getResult().getChildren())
                    {
                        NotificationModel notifModel = notifSnapshot.getValue(NotificationModel.class);
                        notificationModels.add(notifModel);
                        notificationAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
                    Intent intent3= new Intent(NotificationActivity.this, ContextActivity.class);
                    startActivity(intent3);
                    finish();
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
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if(!availability.isSupported()) {
            // Replace with your menu item ID
            bottomNavigationView.getMenu().findItem(R.id.action_search).setVisible(false); // Set the visibility to false to hide the menu item
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        /*if(id == R.id.menu_chat)
        {
            //mAuth.signOut();
            Intent intent = new Intent(NotificationActivity.this, ChatActivity.class);
            startActivity(intent);
            // finish();
        }*/
        if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(NotificationActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }
}