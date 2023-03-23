package my.fa250.furniture4u.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.furniture4u.R;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.comAdapter.CategoryAdapter;
import my.fa250.furniture4u.model.CategoryModel;

public class HomePageActivity extends AppCompatActivity {

    RecyclerView catRecyclerview;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initUI();
    }

    private void initUI()
    {
        ImageSlider imgSlider = findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();
        catRecyclerview = findViewById(R.id.rec_category);

        slideModels.add(new SlideModel(R.drawable.wakeupcat,"TEST", ScaleTypes.CENTER_CROP));

        imgSlider.setImageList(slideModels);

        catRecyclerview.setLayoutManager(new LinearLayoutManager(HomePageActivity.this,RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(HomePageActivity.this, categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);
    }
}