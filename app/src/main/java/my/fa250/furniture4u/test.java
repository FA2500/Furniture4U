package my.fa250.furniture4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.FacebookSdk;

import my.fa250.furniture4u.arsv.ARActivity2;
import my.fa250.furniture4u.auth.MultipleSignInActivity;
import my.fa250.furniture4u.ml.ContextActivity;
import my.fa250.furniture4u.mltest.MlTest;


public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FacebookSdk.sdkInitialize(getApplicationContext());


        //AppEventsLogger.activateApp(this);

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);*/
    }

    public void goToAR(View v)
    {
        Intent intent = new Intent(test.this , ARActivity2.class);
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
        Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
        String a = "https://furniture4u.s3.ap-southeast-1.amazonaws.com/table/glass/table_glass_grey.gltf" ;
        sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=".concat(a)));
        //sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://furniture4u.s3.ap-southeast-1.amazonaws.com/couch/m2/red/couch_m2_red.gltf"));
        //sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF/SheenChair.gltf"));
        sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
        startActivity(sceneViewerIntent);
        //Intent intent = new Intent(test.this , ARActivity2.class);
        //startActivity(intent);

        /*Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
        Uri intentUri =
                Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                        .appendQueryParameter("file", "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
                        .appendQueryParameter("mode", "ar_preferred")
                        .appendQueryParameter("title", "Furniture")
                        .appendQueryParameter("resizable", "false")
                        .build();
        sceneViewerIntent.setData(intentUri);
        sceneViewerIntent.setPackage("com.google.ar.core");
        startActivity(sceneViewerIntent);*/
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