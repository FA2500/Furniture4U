package my.fa250.furniture4u.com;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import my.fa250.furniture4u.MainActivity;
import android.Manifest;
import my.fa250.furniture4u.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import my.fa250.furniture4u.auth.MultipleSignInActivity;
import my.fa250.furniture4u.comAdapter.CategoryAdapter;
import my.fa250.furniture4u.comAdapter.PopAdapter;
import my.fa250.furniture4u.comAdapter.ProductAdapter;
import my.fa250.furniture4u.model.CategoryModel;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;

public class HomePageActivity extends AppCompatActivity {

    List<String> c = new ArrayList<String>();

    //ProgressBar
    ProgressBar progressBar;
    LinearLayout LL;

    //Category
    RecyclerView catRecyclerview;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //Product
    RecyclerView prodRecyclerview;
    ProductAdapter productAdapter;
    List<ProductModel> productModelList;

    //Popular
    RecyclerView popRecyclerview;
    PopAdapter popAdapter;
    List<PopModel> popModelList;

    //Toolbar
    Toolbar toolbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //Database
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

    //TextView
    TextView productShowAll, popShowAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initUI();
        readData();
        askNotificationPermission();
    }

    private void initUI()
    {
        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("HomePage");
        try {
            getSupportActionBar().setTitle("HomePage");
        }
        catch (Exception e)
        {
            Log.w("Action",e);
        }
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //Progress Bar
        LL = findViewById(R.id.home_layout);
        progressBar = new ProgressBar(HomePageActivity.this,null, android.R.attr.progressBarStyle);
        LL.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //TextView
        productShowAll = findViewById(R.id.newProducts_see_all);
        popShowAll = findViewById(R.id.popular_see_all);

        //TV setOnClickListener


        productShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ShowAllActivity.class);
                startActivity(intent);
            }
        });

        popShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ShowAllActivity.class);
                startActivity(intent);
            }
        });

        //Image Slider
        ImageSlider imgSlider = findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        //Banner Data
        slideModels.add(new SlideModel(R.drawable.banner, ScaleTypes.FIT));
        imgSlider.setImageList(slideModels);



        //Recycler View
        catRecyclerview = findViewById(R.id.rec_category);
        prodRecyclerview = findViewById(R.id.new_product_rec);
        popRecyclerview = findViewById(R.id.popular_rec);

        //Recycler View Manager`
        catRecyclerview.setLayoutManager(new LinearLayoutManager(HomePageActivity.this,RecyclerView.HORIZONTAL,false));
        prodRecyclerview.setLayoutManager(new LinearLayoutManager(HomePageActivity.this,RecyclerView.HORIZONTAL,false));
        popRecyclerview.setLayoutManager(new GridLayoutManager(HomePageActivity.this,2));

        //Category UI init
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(HomePageActivity.this, categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        //Product UI Init
        productModelList = new ArrayList<>();
        productAdapter = new ProductAdapter(HomePageActivity.this, productModelList);
        prodRecyclerview.setAdapter(productAdapter);

        //Pop UI Init
        popModelList = new ArrayList<>();
        popAdapter = new PopAdapter(HomePageActivity.this, popModelList);
        popRecyclerview.setAdapter(popAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    // Handle home item click
                    return true;
                case R.id.action_search:
                    // Handle search item click
                    return true;
                case R.id.action_notifications:
                    // Handle notifications item click
                    Intent intent1 = new Intent(HomePageActivity.this, NotificationActivity.class);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.action_profile:
                    // Handle profile item click
                    Intent intent2 = new Intent(HomePageActivity.this, ProfileActivity.class);
                    startActivity(intent2);
                    finish();
                    return true;
            }
            return false;
        });
    }

    /*private void writeData() {
        HashMap<Object,String> a = new HashMap<Object, String>() ;
        a.put("name","Bed");
        a.put("type","Bed");
        a.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fbed.jpg?alt=media&token=50aa5caa-00a7-4731-9069-ca96cac9e906");
        database.getReference().child("category").child("Bed").setValue(a);

        HashMap<Object,String> b = new HashMap<Object, String>() ;
        b.put("name","Table");
        b.put("type","Table");
        b.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Ftable.jpg?alt=media&token=6c3eaeac-b4de-458a-9868-91e77040d04f");
        database.getReference().child("category").child("Table").setValue(b);

        HashMap<Object,String> c = new HashMap<Object, String>() ;
        c.put("name","Couch");
        c.put("type","Couch");
        c.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fsofa.jpg?alt=media&token=667a1091-cb50-4bc5-9689-a970beee9145");
        database.getReference().child("category").child("Couch").setValue(c);

        HashMap<Object,String> d = new HashMap<Object, String>() ;
        d.put("name","Chair");
        d.put("type","Chair");
        d.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fchair.jpg?alt=media&token=bda7bac3-10e9-4bfc-b386-c91e5080a3b6");
        database.getReference().child("category").child("Chair").setValue(d);

        CategoryModel user1 = new CategoryModel();
        user1.setImg_url("https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fbed.jpg?alt=media&token=50aa5caa-00a7-4731-9069-ca96cac9e906");
        user1.setName("Bed");
        user1.setType("Bed");

        CategoryModel user2 = new CategoryModel();
        user2.setImg_url("https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Ftable.jpg?alt=media&token=6c3eaeac-b4de-458a-9868-91e77040d04f");
        user2.setName("Table");
        user2.setType("Table");

        CategoryModel user3 = new CategoryModel();
        user3.setImg_url("https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fsofa.jpg?alt=media&token=667a1091-cb50-4bc5-9689-a970beee9145");
        user3.setName("Couch");
        user3.setType("Couch");

        CategoryModel user4 = new CategoryModel();
        user4.setImg_url("https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fchair.jpg?alt=media&token=bda7bac3-10e9-4bfc-b386-c91e5080a3b6");
        user4.setName("Chair");
        user4.setType("Chair");

        database.getReference().child("category").child("Bed").setValue(user1);
        database.getReference().child("category").child("Table").setValue(user2);
        database.getReference().child("category").child("Couch").setValue(user3);
        database.getReference().child("category").child("Chair").setValue(user4);

        /*HashMap<Object,String> a = new HashMap<Object, String>() ;
        a.put("name","Bed");
        a.put("type","Bed");
        a.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fbed.jpg?alt=media&token=50aa5caa-00a7-4731-9069-ca96cac9e906");
        database.getReference().child("category").child("Bed").setValue(a);

        HashMap<Object,String> b = new HashMap<Object, String>() ;
        b.put("name","Table");
        b.put("type","Table");
        b.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Ftable.jpg?alt=media&token=6c3eaeac-b4de-458a-9868-91e77040d04f");
        database.getReference().child("category").child("Table").setValue(b);

        HashMap<Object,String> c = new HashMap<Object, String>() ;
        c.put("name","Couch");
        c.put("type","Couch");
        c.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fsofa.jpg?alt=media&token=667a1091-cb50-4bc5-9689-a970beee9145");
        database.getReference().child("category").child("Couch").setValue(c);

        HashMap<Object,String> d = new HashMap<Object, String>() ;
        d.put("name","Chair");
        d.put("type","Chair");
        d.put("img_url","https://firebasestorage.googleapis.com/v0/b/furniture4u-93724.appspot.com/o/category%2Fchair.jpg?alt=media&token=bda7bac3-10e9-4bfc-b386-c91e5080a3b6");
        database.getReference().child("category").child("Chair").setValue(d);
    }*/

    /*private void getData()
    {
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot doc : task.getResult())
                            {
                                CategoryModel catMod = doc.toObject(CategoryModel.class);
                                Log.d("DB",catMod.toString());
                                categoryModelList.add(catMod);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        DatabaseReference myRef = database.getReference("category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("RT", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RT", "Failed to read value.", error.toException());
            }
        });
    }*/

    private void readData()
    {
        database.getReference().child("categorylist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    List<String> a = (List<String>) task.getResult().getValue();
                    c.addAll(a) ;
                    displaydata();
                }
                else
                {
                    Toast.makeText(HomePageActivity.this, "Failed to retrieve data. Limited internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    ProductModel productModel = snapshot1.getValue(ProductModel.class);
                    productModelList.add(productModel);
                    productAdapter.notifyDataSetChanged();

                    PopModel popModel = snapshot1.getValue(PopModel.class);
                    popModelList.add(popModel);
                    popAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "Error fetching data"+ error);
            }
        };
        database.getReference("product").addListenerForSingleValueEvent(valueEventListener);
    }

    private void displaydata()
    {
        for(int i = 0 ; i < c.size() ; i++)
        {
            database.getReference().child("category").child(c.get(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        CategoryModel catMod = task.getResult().getValue(CategoryModel.class);
                        categoryModelList.add(catMod);
                        categoryAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(HomePageActivity.this, "Failed to retrieve data. Limited internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        progressBar.setVisibility(View.GONE);
        LL.setVisibility(View.VISIBLE);

    }

    public void goToSearch(View v)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to quit the application?");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // [START ask_post_notifications]
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    // [END ask_post_notifications]

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_chat)
        {
            //mAuth.signOut();
            Intent intent = new Intent(HomePageActivity.this, ChatActivity.class);
            startActivity(intent);
           // finish();
        }
        else if(id==R.id.menu_cart)
        {
            Intent intent = new Intent(HomePageActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }


}