package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView detailImage,addItem,removeItem;
    TextView rating,desc,price,name;
    Button addToCart,buyNow;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    Object productInfo;
    ProductModel productModel;
    PopModel popModel;

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
        detailImage = findViewById(R.id.product_detail_img);
        addItem = findViewById(R.id.product_detail_increase);
        removeItem = findViewById(R.id.product_detail_decrease);
        rating = findViewById(R.id.product_detail_rateNo);
        desc = findViewById(R.id.product_detail_desc);
        price = findViewById(R.id.product_detail_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        name = findViewById(R.id.product_detail_name);
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
        if(productModel != null)
        {
            Glide.with(this).load(productModel.getImg_url()).into(detailImage);
            name.setText(productModel.getName());
            price.setText(String.valueOf(productModel.getPrice()));
            rating.setText(String.valueOf(productModel.getRating()));
            desc.setText(productModel.getDescription());
        }
        if(popModel != null)
        {
            Glide.with(this).load(popModel.getImg_url()).into(detailImage);
            name.setText(popModel.getName());
            price.setText(String.valueOf(popModel.getPrice()));
            rating.setText(String.valueOf(popModel.getRating()));
            desc.setText(popModel.getDescription());
        }
    }


}