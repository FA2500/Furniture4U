package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;
import my.fa250.furniture4u.model.ShowAllModel;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView detailImage,addItem,removeItem;
    TextView rating,desc,price,name,quantity;
    Button addToCart,buyNow;

    //Firebase
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Object productInfo;

    //Info Model
    ProductModel productModel;
    PopModel popModel;
    ShowAllModel allModel;

    //var
    int totalQuantity = 1;
    double totalprice = 0;
    double baseprice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productInfo = getIntent().getSerializableExtra("details");
        initUI();
        readData();
    }

    private void initUI()
    {
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

        //setOnClickListener
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
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
                    price.setText(String.valueOf(totalprice));
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
                    price.setText(String.valueOf(totalprice));
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
        if(productModel != null)
        {
            Glide.with(this).load(productModel.getImg_url()).into(detailImage);
            name.setText(productModel.getName());
            price.setText(String.valueOf(productModel.getPrice()));
            rating.setText(String.valueOf(productModel.getRating()));
            desc.setText(productModel.getDescription());

            baseprice = productModel.getPrice();
        }
        else if(popModel != null)
        {
            Glide.with(this).load(popModel.getImg_url()).into(detailImage);
            name.setText(popModel.getName());
            price.setText(String.valueOf(popModel.getPrice()));
            rating.setText(String.valueOf(popModel.getRating()));
            desc.setText(popModel.getDescription());

            baseprice = popModel.getPrice() ;
        }
        else if(allModel != null)
        {
            Glide.with(this).load(allModel.getImg_url()).into(detailImage);
            name.setText(allModel.getName());
            price.setText(String.valueOf(allModel.getPrice()));
            rating.setText(String.valueOf(allModel.getRating()));
            desc.setText(allModel.getDescription());

            baseprice = allModel.getPrice();
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

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",baseprice);
        cartMap.put("currentDate",currentDate);
        cartMap.put("currentTime",currentTime);
        cartMap.put("totalQuantity",totalQuantity);
        cartMap.put("totalPrice",totalprice);

        Log.d("mAuth",mAuth.getCurrentUser().getUid());

        firestore.collection("user/"+mAuth.getCurrentUser().getUid()+"/cart")
                .add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetailActivity.this, "Item successfully added to cart",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }


}