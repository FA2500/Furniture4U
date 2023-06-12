package my.fa250.furniture4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import my.fa250.furniture4u.auth.MultipleSignInActivity;
import my.fa250.furniture4u.com.HomePageActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView Img;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        Img = findViewById(R.id.splashlogo);
        Animation anime = AnimationUtils.loadAnimation(this,R.anim.side_slide);
        Img.startAnimation(anime);

        Intent i ;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            i = new Intent(SplashScreen.this, HomePageActivity.class);
        }
        else
        {
            i = new Intent(SplashScreen.this, MultipleSignInActivity.class);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 3000);

    }


}
