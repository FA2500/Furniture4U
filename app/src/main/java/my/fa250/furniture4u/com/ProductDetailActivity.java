package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.CartModel;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;
import my.fa250.furniture4u.model.ShowAllModel;

public class ProductDetailActivity extends AppCompatActivity {

    Toolbar toolbar;


    ImageView addItem,removeItem;

    ImageSlider detailImage;
    List<SlideModel> slideModels = new ArrayList<>();
    TextView rating,desc,price,name,quantity;
    Button addToCart,buyNow;

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

    //var
    int totalQuantity = 1;
    double totalprice = 0;
    double baseprice = 0;

    String img_url;
    Boolean isInCart;

    DecimalFormat DF = new DecimalFormat(".00");


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
            slideModels.add(new SlideModel(productModel.getImg_url(),ScaleTypes.FIT));
            detailImage.setImageList(slideModels);
            name.setText(productModel.getName());
            //price.setText(String.valueOf(productModel.getPrice()));
            price.setText(DF.format(productModel.getPrice()));
            rating.setText(String.valueOf(productModel.getRating()));
            ratingBar.setRating(Float.parseFloat(productModel.getRating().toString()));
            desc.setText(productModel.getDescription());

            baseprice = productModel.getPrice();
            img_url = productModel.getImg_url();
        }
        else if(popModel != null)
        {
            slideModels.clear();
            slideModels.add(new SlideModel(popModel.getImg_url(),ScaleTypes.FIT));
            detailImage.setImageList(slideModels);
            name.setText(popModel.getName());
            price.setText(DF.format(popModel.getPrice()));
            rating.setText(String.valueOf(popModel.getRating()));
            ratingBar.setRating(Float.parseFloat(popModel.getRating().toString()));
            desc.setText(popModel.getDescription());

            baseprice = popModel.getPrice() ;
            img_url = popModel.getImg_url();
        }
        else if(allModel != null)
        {
            slideModels.clear();
            slideModels.add(new SlideModel(allModel.getImg_url(),ScaleTypes.FIT));
            detailImage.setImageList(slideModels);
            name.setText(allModel.getName());
            price.setText(DF.format(allModel.getPrice()));
            rating.setText(String.valueOf(allModel.getRating()));
            float a = (float) allModel.getRating();
            ratingBar.setRating(a);
            desc.setText(allModel.getDescription());

            baseprice = allModel.getPrice();
            img_url = allModel.getImg_url();
        }
        else if(cartModel != null)
        {
           // Glide.with(this).load(cartModel.getImg_url()).into(detailImage);
            slideModels.clear();
            slideModels.add(new SlideModel(cartModel.getImg_url(),ScaleTypes.FIT));
            detailImage.setImageList(slideModels);
            name.setText(cartModel.getProductName());
            price.setText(DF.format(cartModel.getProductPrice()));
            rating.setText(String.valueOf(cartModel.getRating()));
            float a = (float) cartModel.getRating();
            ratingBar.setRating(a);
            desc.setText(cartModel.getDescription());

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


}