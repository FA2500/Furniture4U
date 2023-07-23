package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DecimalFormat;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.AddressModel;
import my.fa250.furniture4u.model.InvoiceModel;

public class InvoiceActivity extends AppCompatActivity implements Serializable {

    Toolbar toolbar;
    TextView inID, inTotal,inDate, inAdd, inName, inQuan, inVar;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String ID;
    DecimalFormat DF = new DecimalFormat(".00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        ID = getIntent().getStringExtra("ID");

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.invoice_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Invoice");
        try {
            getSupportActionBar().setTitle("Invoice");
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

        inID = findViewById(R.id.invoiceID);
        inTotal = findViewById(R.id.invoiceTotal);
        inDate = findViewById(R.id.invoiceDate);
        inAdd = findViewById(R.id.invoiceAddress);
        inName = findViewById(R.id.invoiceName);
        inQuan = findViewById(R.id.invoiceQuans);
        inVar = findViewById(R.id.invoiceVar);
    }

    private void getData()
    {
        database.getReference("user").child(mAuth.getUid()).child("order").child(ID).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().exists())
                        {
                            InvoiceModel im = task.getResult().getValue(InvoiceModel.class);
                            im.setId(task.getResult().getKey());
                            database.getReference("user").child(mAuth.getUid()).child("address").child(im.getAddress()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.getResult().exists())
                                            {
                                                AddressModel am = task.getResult().getValue(AddressModel.class);
                                                im.setAddress(am.getAddress()+","+am.getCode()+","+am.getDistrict()+","+am.getState());
                                                updateUI(im);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void updateUI(InvoiceModel invoiceModel)
    {
        inID.setText("Order ID: "+invoiceModel.getId());
        inTotal.setText("RM"+DF.format((double) invoiceModel.getTotalPrice()));
        inDate.setText(invoiceModel.getCurrentDate());
        inAdd.setText(invoiceModel.getAddress());
        inName.setText(invoiceModel.getProductName());
        inQuan.setText(""+invoiceModel.getTotalQuantity());
        inVar.setText(invoiceModel.getColour());
    }
}