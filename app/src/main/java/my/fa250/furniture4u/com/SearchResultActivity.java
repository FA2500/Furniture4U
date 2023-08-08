package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.ShowAllAdapter;
import my.fa250.furniture4u.model.SearchModel;
import my.fa250.furniture4u.model.ShowAllModel;

public class SearchResultActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    List<ShowAllModel> showAllModelList2;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initUI();
        getData();
    }

    private void initUI()
    {
        //toolbar
        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        type = getIntent().getStringExtra("query");

        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList = new ArrayList<>();
        showAllModelList2 = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);
    }

    private void getData()
    {
        Query query = database.getReference("product");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ShowAllModel showAllModel = dataSnapshot.getValue(ShowAllModel.class);
                    showAllModelList2.add(showAllModel);
                   // showAllModelList.add(showAllModel);
                    //showAllAdapter.notifyDataSetChanged();
                }
                searchQuery(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Database", "Error reading data", error.toException());
            }
        });

    }

    private void searchQuery(String query)
    {
        for(int i = 0 ; i < showAllModelList2.size() ; i++)
        {
            if(showAllModelList2.get(i).getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || showAllModelList2.get(i).getType().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
            {
                ShowAllModel sm = showAllModelList2.get(i);
                showAllModelList.add(sm);
                showAllAdapter.notifyDataSetChanged();
            }
        }
    }
}