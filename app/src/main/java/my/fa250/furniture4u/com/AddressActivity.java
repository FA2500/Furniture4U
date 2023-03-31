package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import my.fa250.furniture4u.R;

public class AddressActivity extends AppCompatActivity {

    Button addAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initUI();
    }

    private void initUI()
    {
        addAddress = findViewById(R.id.add_address_btn);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
    }
}