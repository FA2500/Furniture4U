package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

import my.fa250.furniture4u.R;

public class InvoiceActivity extends AppCompatActivity implements Serializable {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.detail_toolbar);
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
    }

    private void getData()
    {

    }
}