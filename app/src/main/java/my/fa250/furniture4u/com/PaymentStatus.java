package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import my.fa250.furniture4u.R;

public class PaymentStatus extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        tv = findViewById(R.id.paymentstatusTV);
        imageView = findViewById(R.id.paymentStatusIMG);

        String status = getIntent().getStringExtra("status");
        if(Objects.equals(status, "success"))
        {
            tv.setText("Payment received. Thank you for your purchase.");
            imageView.setImageResource(R.drawable.checkmark_success);
        }
        else if(Objects.equals(status, "failed"))
        {
            tv.setText("Payment failed. Please try again later.");
            imageView.setImageResource(R.drawable.cross_failed);
        }
    }

    public void goBack(View v)
    {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}