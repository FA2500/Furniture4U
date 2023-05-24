package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.CartAdapter;
import my.fa250.furniture4u.comAdapter.ProfileAdapter;
import my.fa250.furniture4u.model.ProfileModel;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    List<ProfileModel> profileModelList;
    ProfileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        try {
            getSupportActionBar().setTitle("Profile");
        }
        catch (Exception e)
        {
            Log.w("Action",e);
        }

        recyclerView = findViewById(R.id.order_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this,RecyclerView.HORIZONTAL,false));
        profileModelList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, profileModelList);
        recyclerView.setAdapter(profileAdapter);

        profileModelList.add(new ProfileModel(R.drawable.baseline_payment_24, "To Pay", 0));
        profileModelList.add(new ProfileModel(R.drawable.baseline_alarm_24, "Preparing", 0));
        profileModelList.add(new ProfileModel(R.drawable.baseline_local_shipping_24, "Shipping", 0));
        profileModelList.add(new ProfileModel(R.drawable.baseline_rate_review_24, "Received", 0));
        profileAdapter.notifyDataSetChanged();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    // Handle home item click
                    Intent intent = new Intent(ProfileActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.action_search:
                    // Handle search item click
                    return true;
                case R.id.action_notifications:
                    // Handle notifications item click
                    return true;
                case R.id.action_profile:
                    // Handle profile item click
                    return true;
            }
            return false;
        });
    }
}