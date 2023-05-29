package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.PreparingAdapter;
import my.fa250.furniture4u.model.CartModel;
import my.fa250.furniture4u.model.PreparingModel;

public class PreparingActivity extends AppCompatActivity implements Serializable {

    //toolbar
    Toolbar toolbar;

    //TV
    TextView totalPriceTV;

    private double qwerty = 0.0;

    //firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //adapter
    RecyclerView recyclerView;
    List<CartModel> prepareModelList;
    PreparingAdapter prepareAdapter;

    List<String> listID = new ArrayList<>();

    //var
    double overallTotalAmount;
    DecimalFormat DF = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order");
        try {
            getSupportActionBar().setTitle("Order");
        }
        catch (Exception e)
        {
            Log.w("Action",e);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prepareModelList = new ArrayList<>();
        prepareAdapter = new PreparingAdapter(this, prepareModelList);
        recyclerView.setAdapter(prepareAdapter);
    }

    private void getData()
    {
        database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel preparingModel = dataSnapshot.getValue(CartModel.class);
                    preparingModel.setId(dataSnapshot.getKey());
                    prepareModelList.add(preparingModel);
                    prepareAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Error reading order data", error.toException());
            }
        });
    }
}