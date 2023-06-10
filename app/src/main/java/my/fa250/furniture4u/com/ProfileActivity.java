package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.comAdapter.CartAdapter;
import my.fa250.furniture4u.comAdapter.ProfileAdapter;
import my.fa250.furniture4u.model.CartModel;
import my.fa250.furniture4u.model.ProfileModel;

public class ProfileActivity extends AppCompatActivity {

    //UI
    Toolbar toolbar;
    TextView usernameTV,registeredTV;

    Button AccountSettingBtn, FavouriteBtn, HelpBtn, LogoutBtn;

    RecyclerView recyclerView;
    List<ProfileModel> profileModelList;
    ProfileAdapter profileAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");



    //counter
    private int payCounter = 0;
    private int preCounter = 0;
    private int shipCounter= 0;
    private int recCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        try {
            getSupportActionBar().setTitle("Profile");
        }
        catch (Exception e)
        {
            Log.w("Action",e);
        }

        usernameTV = findViewById(R.id.pro_username_tv);
        registeredTV = findViewById(R.id.pro_registered_tv);
        usernameTV.setText(UserInfo.getName());
        registeredTV.setText(UserInfo.getEmail());

        recyclerView = findViewById(R.id.order_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this,RecyclerView.HORIZONTAL,false));
        profileModelList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, profileModelList);
        recyclerView.setAdapter(profileAdapter);

        //Button
        AccountSettingBtn = findViewById(R.id.pro_setting_btn);
        FavouriteBtn = findViewById(R.id.pro_fav_btn);
        HelpBtn = findViewById(R.id.pro_help_btn);
        LogoutBtn = findViewById(R.id.pro_logout_btn);

        AccountSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        //Button

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    // Handle home item click
                    Intent intent = new Intent(ProfileActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.action_search:
                    // Handle search item click
                    return true;
                case R.id.action_notifications:
                    // Handle notifications item click
                    Intent intent1 = new Intent(ProfileActivity.this, NotificationActivity.class);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.action_profile:
                    // Handle profile item click
                    return true;
            }
            return false;
        });
    }

    private void getData()
    {
        database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    preCounter++;
                }
                //profileModelList.add(new ProfileModel(R.drawable.baseline_shopping_cart_24, "In Cart", 0));
                profileModelList.add(new ProfileModel(R.drawable.baseline_payment_24, "To Pay", 0));
                profileModelList.add(new ProfileModel(R.drawable.baseline_alarm_24, "Preparing", preCounter));
                profileModelList.add(new ProfileModel(R.drawable.baseline_local_shipping_24, "Shipping", 0));
                profileModelList.add(new ProfileModel(R.drawable.baseline_rate_review_24, "Delivered", 0));
                profileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Error reading cart data", error.toException());
            }
        });
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
            Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
            startActivity(intent);
            // finish();
        }
        else if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }
}