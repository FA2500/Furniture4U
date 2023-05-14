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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.CartAdapter;
import my.fa250.furniture4u.model.CartModel;

public class CartActivity extends AppCompatActivity {

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
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;

    //var
    double overallTotalAmount;
    DecimalFormat DF = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cart");
        try {
            getSupportActionBar().setTitle("Cart");
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
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

        totalPriceTV = findViewById(R.id.cartTotalPrice);
        totalPriceTV.setText("Total Amount : RM"+DF.format(0.00));

        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("CartTotalAmount"));
    }

    public void buyNow(View v)
    {
        Intent intent = new Intent(CartActivity.this, AddressActivity.class);
        startActivity(intent);
    }

    private void getData()
    {
        ///user/mvr4KsEdNNeuBDYzLIAZ2G4p5uI2/cart/jJfXe0TegK9uYayLFKt4

        /*firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot doc : task.getResult())
                            {
                                CartModel cartModel = doc.toObject(CartModel.class);
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });*/

        database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                    cartModelList.add(cartModel);
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Error reading cart data", error.toException());
            }
        });
    }

    public BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double totalBill = intent.getDoubleExtra("totalAmount",0.0);
            Log.d("INTENT","RECEIVING VALUE "+totalBill);
            qwerty = qwerty + totalBill;
            totalPriceTV.setText("Total Amount : RM"+DF.format(qwerty));
        }
    };
}