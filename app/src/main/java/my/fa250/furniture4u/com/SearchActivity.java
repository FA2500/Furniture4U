package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.SearchAdapter;
import my.fa250.furniture4u.model.PopModel;
import my.fa250.furniture4u.model.ProductModel;
import my.fa250.furniture4u.model.SearchModel;

public class SearchActivity extends AppCompatActivity {
    private SearchView Sv;

    //Search
    RecyclerView seaRecyclerView;
    SearchAdapter searchAdapter;
    List<SearchModel> searchModelList;

    List<SearchModel> searchModelList2;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

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
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery(newText);
                return false;
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