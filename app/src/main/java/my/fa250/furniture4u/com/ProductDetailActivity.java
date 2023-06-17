package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.ar.core.ArCoreApk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.arsv.ARActivity2;
import my.fa250.furniture4u.comAdapter.CategoryAdapter;
import my.fa250.furniture4u.comAdapter.ProductAdapter;
import my.fa250.furniture4u.comAdapter.VarianceAdapter;
import my.fa250.furniture4u.model.CartModel;
import my.fa250.furniture4u.model.CategoryModel;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;
import my.fa250.furniture4u.model.ShowAllModel;
import my.fa250.furniture4u.model.VarianceModel;
import my.fa250.furniture4u.test;

public class ProductDetailActivity extends AppCompatActivity {

    Toolbar toolbar;

    ImageView addItem,removeItem;

    ImageSlider detailImage;
    List<SlideModel> slideModels = new ArrayList<>();
    TextView rating,desc,price,name,quantity;
    Button addToCart,buyNow,viewIn3D,viewInAR;

    LinearLayout varianceLayout;

    RatingBar ratingBar;

    //Firebase
    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Object productInfo;

    //Info Model
    ProductModel productModel;
    PopModel popModel;
    ShowAllModel allModel;

    CartModel cartModel;

    String varianceChosen,productID,productCat,url_3d;

    //var
    int totalQuantity = 1;
    double totalprice = 0;
    double baseprice = 0;

    List<String> img_url;
    Boolean isInCart;

    DecimalFormat DF = new DecimalFormat(".00");

    //Product
    RecyclerView varRecyclerView;
    VarianceAdapter varianceAdapter;
    List<VarianceModel> varianceModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productInfo = getIntent().getSerializableExtra("details");
        Log.d("Info",productInfo.toString());
        initUI();
        readData();
    }

    private void initUI()
    {
        //toolbar
        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quantity = findViewById(R.id.product_detail_quantity);
        detailImage = findViewById(R.id.product_detail_img);
        addItem = findViewById(R.id.product_detail_increase);
        removeItem = findViewById(R.id.product_detail_decrease);
        rating = findViewById(R.id.product_detail_rateNo);
        desc = findViewById(R.id.product_detail_desc);
        price = findViewById(R.id.product_detail_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        name = findViewById(R.id.product_detail_name);
        ratingBar = findViewById(R.id.product_detail_rate);
        varianceLayout = findViewById(R.id.varianceLayout);
        varRecyclerView = findViewById(R.id.varianceRecyclerView);
        viewIn3D = findViewById(R.id.view_in_3d_btn);
        viewInAR = findViewById(R.id.view_in_ar_btn);
        varRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this,RecyclerView.HORIZONTAL,false));
        //setOnClickListener
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity >= 1)
                {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalprice = baseprice*totalQuantity;
                    price.setText(DF.format(totalprice));
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1)
                {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalprice = baseprice*totalQuantity;
                    price.setText(DF.format(totalprice));
                }
            }
        });

        viewIn3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
                sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=".concat(url_3d)));
                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
                startActivity(sceneViewerIntent);
            }
        });

        viewInAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this , ARActivity2.class);
                intent.putExtra("url_3d",url_3d);
                startActivity(intent);
            }
        });

        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if(availability.isSupported()) {
            viewIn3D.setVisibility(View.VISIBLE);
            viewInAR.setVisibility(View.VISIBLE);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("VarianceButtonClick"));
    }

    private void readData()
    {
        if(productInfo instanceof ProductModel )
        {
            productModel = (ProductModel) productInfo;
        }
        else if(productInfo instanceof PopModel)
        {
            popModel = (PopModel) productInfo;
        }
        else if(productInfo instanceof ShowAllModel)
        {
            allModel = (ShowAllModel) productInfo;
        }
        else if(productInfo instanceof  CartModel)
        {
            cartModel = (CartModel) productInfo;
        }
        if(productModel != null)
        {
            slideModels.clear();
            for(int i = 0 ; i < productModel.getImg_url().size() ; i++)
            {
                slideModels.add(new SlideModel(productModel.getImg_url().get(i), ScaleTypes.FIT));
            }
            detailImage.setImageList(slideModels);
            name.setText(productModel.getName());
            //price.setText(String.valueOf(productModel.getPrice()));
            price.setText(DF.format(productModel.getPrice()));
            rating.setText(String.valueOf(productModel.getRating()));
            ratingBar.setRating(Float.parseFloat(productModel.getRating().toString()));
            desc.setText(productModel.getDescription());
            productID = productModel.getID();
            productCat = productModel.getCategory();
            url_3d = productModel.getUrl_3d();

            Log.d("Variance 1", "" + productModel.getVariance().get(0));
            if(!productModel.getVariance().isEmpty() && !Objects.equals(productModel.getVariance().get(0), "null"))
            {
                varianceLayout.setVisibility(View.VISIBLE);
                varianceModelList = new ArrayList<>();
                varianceAdapter = new VarianceAdapter(ProductDetailActivity.this, varianceModelList);
                varRecyclerView.setAdapter(varianceAdapter);
                getVarianceData(productModel);
                addToCart.setClickable(false);
                addToCart.setEnabled(false);
                buyNow.setClickable(false);
                buyNow.setEnabled(false);
            }

            baseprice = productModel.getPrice();
            img_url = productModel.getImg_url();
        }
        else if(popModel != null)
        {
            slideModels.clear();
            for(int i = 0 ; i < popModel.getImg_url().size() ; i++)
            {
                slideModels.add(new SlideModel(popModel.getImg_url().get(i), ScaleTypes.FIT));
            }
            detailImage.setImageList(slideModels);
            name.setText(popModel.getName());
            price.setText(DF.format(popModel.getPrice()));
            rating.setText(String.valueOf(popModel.getRating()));
            ratingBar.setRating(Float.parseFloat(popModel.getRating().toString()));
            desc.setText(popModel.getDescription());
            productID = popModel.getID();
            productCat = popModel.getCategory();
            url_3d = popModel.getUrl_3d();

            if(!popModel.getVariance().isEmpty() && !Objects.equals(popModel.getVariance().get(0), "null"))
            {
                varianceLayout.setVisibility(View.VISIBLE);
                varianceModelList = new ArrayList<>();
                varianceAdapter = new VarianceAdapter(ProductDetailActivity.this, varianceModelList);
                varRecyclerView.setAdapter(varianceAdapter);
                getVarianceData(popModel);
                addToCart.setClickable(false);
                addToCart.setEnabled(false);
                buyNow.setClickable(false);
                buyNow.setEnabled(false);
            }

            baseprice = popModel.getPrice() ;
            img_url = popModel.getImg_url();
        }
        else if(allModel != null)
        {
            slideModels.clear();
            for(int i = 0 ; i < allModel.getImg_url().size() ; i++)
            {
                slideModels.add(new SlideModel(allModel.getImg_url().get(i), ScaleTypes.FIT));
            }
            detailImage.setImageList(slideModels);
            name.setText(allModel.getName());
            price.setText(DF.format(allModel.getPrice()));
            rating.setText(String.valueOf(allModel.getRating()));
            float a = (float) allModel.getRating();
            ratingBar.setRating(a);
            desc.setText(allModel.getDescription());
            productID = allModel.getID();
            productCat = allModel.getCategory();
            url_3d = allModel.getUrl_3d();

            if(!allModel.getVariance().isEmpty() && !Objects.equals(allModel.getVariance().get(0), "null"))
            {
                varianceLayout.setVisibility(View.VISIBLE);
                varianceModelList = new ArrayList<>();
                varianceAdapter = new VarianceAdapter(ProductDetailActivity.this, varianceModelList);
                varRecyclerView.setAdapter(varianceAdapter);
                getVarianceData(allModel);
                addToCart.setClickable(false);
                addToCart.setEnabled(false);
                buyNow.setClickable(false);
                buyNow.setEnabled(false);
            }

            baseprice = allModel.getPrice();
            img_url = allModel.getImg_url();
        }
        else if(cartModel != null)
        {
           // Glide.with(this).load(cartModel.getImg_url()).into(detailImage);
            slideModels.clear();
            //slideModels.add(new SlideModel(cartModel.getImg_url(),ScaleTypes.FIT));
            for(int i = 0 ; i < cartModel.getImg_url().size() ; i++)
            {
                slideModels.add(new SlideModel(cartModel.getImg_url().get(i), ScaleTypes.FIT));
            }
            detailImage.setImageList(slideModels);
            name.setText(cartModel.getProductName());
            price.setText(DF.format(cartModel.getProductPrice()));
            rating.setText(String.valueOf(cartModel.getRating()));
            float a = (float) cartModel.getRating();
            ratingBar.setRating(a);
            desc.setText(cartModel.getDescription());
            productID = cartModel.getProductID();
            productCat = cartModel.getProductCat();
            url_3d = cartModel.getUrl_3d();

            if(!cartModel.getVariance().isEmpty() && !Objects.equals(cartModel.getVariance().get(0), "null"))
            {
                varianceLayout.setVisibility(View.VISIBLE);
                varianceModelList = new ArrayList<>();
                varianceAdapter = new VarianceAdapter(ProductDetailActivity.this, varianceModelList);
                varRecyclerView.setAdapter(varianceAdapter);
                getVarianceData(cartModel);
                addToCart.setClickable(false);
                addToCart.setEnabled(false);
                buyNow.setClickable(false);
                buyNow.setEnabled(false);
            }

            baseprice = cartModel.getProductPrice();
            img_url = cartModel.getImg_url();
            isInCart = cartModel.getIsInCart();
            if(isInCart)
            {
                addToCart.setText("View in Cart");
                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void getVarianceData(Object productInfo)
    {
        if(productInfo instanceof ProductModel)
        {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        VarianceModel varMod = snapshot1.getValue(VarianceModel.class);
                        varianceModelList.add(varMod);
                        varianceAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve variance data. Limited internet connection.", Toast.LENGTH_SHORT).show();
                }
            };
            database.getReference("product").child(((ProductModel) productInfo).getID()).child("varianceList").addListenerForSingleValueEvent(valueEventListener);
        }
        else if(productInfo instanceof PopModel)
        {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        VarianceModel varMod = snapshot1.getValue(VarianceModel.class);
                        varianceModelList.add(varMod);
                        varianceAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve variance data. Limited internet connection.", Toast.LENGTH_SHORT).show();
                }
            };
            database.getReference("product").child(((PopModel) productInfo).getID()).child("varianceList").addListenerForSingleValueEvent(valueEventListener);
        }
        else if(productInfo instanceof ShowAllModel)
        {

        }
        else if(productInfo instanceof  CartModel)
        {

        }


    }

    private void addToCart()
    {
        String currentTime, currentDate;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat currDate = new SimpleDateFormat("dd MM yyyy");
        currentDate = currDate.format(cal.getTime());

        SimpleDateFormat currTime = new SimpleDateFormat("HH:mm:ss a");
        currentTime = currTime.format(cal.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        totalprice = baseprice * totalQuantity;
        isInCart = true;

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productID",productID);
        cartMap.put("productPrice",baseprice);
        cartMap.put("currentDate",currentDate);
        cartMap.put("currentTime",currentTime);
        cartMap.put("totalQuantity",totalQuantity);
        cartMap.put("totalPrice",totalprice);
        cartMap.put("img_url",img_url);
        cartMap.put("rating",Double.valueOf(rating.getText().toString()));
        cartMap.put("description",desc.getText().toString());
        cartMap.put("isInCart",isInCart);

        Log.d("mAuth",mAuth.getCurrentUser().getUid());

        database.getReference("user/"+mAuth.getCurrentUser().getUid()+"/cart")
                .push()
                .setValue(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetailActivity.this, "Item successfully added to cart",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        /*firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/cart")
                .add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetailActivity.this, "Item successfully added to cart",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });*/
    }

    public BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String itemName = intent.getStringExtra("itemName");
            int btnID = intent.getIntExtra("btnId",0);
            Log.d("VARIANCE BROADCAST", "item "+itemName+" from "+btnID);

            varianceChosen = itemName;
            addToCart.setClickable(true);
            addToCart.setEnabled(true);
            buyNow.setClickable(true);
            buyNow.setEnabled(true);

            price.setText(DF.format(intent.getDoubleExtra("itemPrice",0.00)));
            Intent intent2 = new Intent("VarianceButtonClick2");
            intent2.putExtra("btnName",itemName);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }
    };

}