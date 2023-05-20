package my.fa250.furniture4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.protobuf.ExtensionRegistry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import my.fa250.furniture4u.ar.checkARSupported;
import my.fa250.furniture4u.arsv.ARActivity2;
import my.fa250.furniture4u.auth.MultipleSignInActivity;
import my.fa250.furniture4u.com.CartActivity;
import my.fa250.furniture4u.ml.ContextActivity;
import my.fa250.furniture4u.mltest.MlTest;


public class test extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FacebookSdk.sdkInitialize(getApplicationContext());


        //AppEventsLogger.activateApp(this);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "my.fa250.furniture4u",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }*/

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);*/
    }

    public void goToAR(View v)
    {
        Intent intent = new Intent(test.this , checkARSupported.class);
        startActivity(intent);
    }

    public void goToECom(View v)
    {
        Intent intent = new Intent(test.this , MultipleSignInActivity.class);
        startActivity(intent);
    }

    public void goToML(View v)
    {
       Intent intent = new Intent(test.this, ContextActivity.class);
       startActivity(intent);
    }

    public void goToARSV(View v)
    {
        //Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
        //sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://furniture4u.s3.ap-southeast-1.amazonaws.com/couch/m2/red/couch_m2_red.gltf"));
        // sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF/SheenChair.gltf"));
        //sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
        //startActivity(sceneViewerIntent);
        Intent intent = new Intent(test.this , ARActivity2.class);
        startActivity(intent);
    }

    public void goToIncor(View v)
    {
        Intent intent = new Intent(this, MlTest.class );
        startActivity(intent);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_logout)
        {
            mAuth.signOut();
            Intent intent = new Intent(test.this, MultipleSignInActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(test.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }*/
}