package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.SearchAdapter;
import my.fa250.furniture4u.model.SearchModel;

public class SearchActivity extends AppCompatActivity {
    private SearchView Sv;
    private String queryString;

    //Search
    RecyclerView seaRecyclerView;
    SearchAdapter searchAdapter;
    List<SearchModel> searchModelList;
    List<SearchModel> searchModelList2;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

    ImageButton searchBtn,cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        readData();
    }

    private void initUI()
    {
        Sv = findViewById(R.id.SearchView);
        searchBtn = findViewById(R.id.searchBtn);
        cancelBtn = findViewById(R.id.searchCancelBtn);
        seaRecyclerView = findViewById(R.id.sea_rv);
        seaRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL,false));
        searchModelList = new ArrayList<>();
        searchModelList2 = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this, searchModelList);
        seaRecyclerView.setAdapter(searchAdapter);

        Sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("query",query);
                queryString = query;
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 1)
                {
                    newText = newText.substring(0,1).toUpperCase(Locale.ROOT) + newText.substring(1);
                }
                Log.w("SEARCH",newText);
                searchQuery(newText);
                queryString = newText;
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("query",queryString);
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomePageActivity.class);;
                startActivity(intent);
            }
        });
    }

    private void readData()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Log.d("SEARCH",snapshot1.getValue().toString());
                    SearchModel sm = snapshot1.getValue(SearchModel.class);
                    searchModelList2.add(sm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "Error fetching data"+ error);
            }
        };
        database.getReference("product").addListenerForSingleValueEvent(valueEventListener);
    }

    private void searchQuery(String query)
    {
        searchModelList.clear();
        searchAdapter.notifyDataSetChanged();
        for(int i = 0 ; i < searchModelList2.size() ; i++)
        {
            if(query.isEmpty())
            {
                break;
            }
            if(searchModelList2.get(i).getName().contains(query))
            {
                SearchModel sm = searchModelList2.get(i);
                searchModelList.add(sm);
                searchAdapter.notifyDataSetChanged();
            }
        }
    }


}