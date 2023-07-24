package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;

public class UIProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView nameTV,emailTV,phoneTV,providerTV;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiprofile);

        initUI();
        getData();
    }

    private void initUI()
    {
        toolbar = findViewById(R.id.uiprofile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile View");
        try {
            getSupportActionBar().setTitle("Profile View");
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
        nameTV = findViewById(R.id.UIPName);
        emailTV = findViewById(R.id.UIPEmail);
        phoneTV = findViewById(R.id.UIPPhone);
        providerTV = findViewById(R.id.UIPProvider);
        submit = findViewById(R.id.UIPSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEdit();
            }
        });
    }

    private void getData()
    {
        nameTV.setText(UserInfo.getName());
        emailTV.setText(UserInfo.getEmail());
        phoneTV.setText(UserInfo.getPhone());
        providerTV.setText(UserInfo.getProvider());
    }

    private void goToEdit()
    {
        Intent intent = new Intent(UIProfileActivity.this, UIEditProfileActivity.class);
        startActivity(intent);
    }

}