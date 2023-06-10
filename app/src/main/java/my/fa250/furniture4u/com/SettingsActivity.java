package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import my.fa250.furniture4u.R;

public class SettingsActivity extends AppCompatActivity {

    //UI
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();

    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Account Setting");
        try {
            getSupportActionBar().setTitle("Account Setting");
        } catch (Exception e) {
            Log.w("Action", e);
        }
    }
}