package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.comAdapter.ShowAllAdapter;
import my.fa250.furniture4u.model.ShowAllModel;

public class ShowAllActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    //FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        initUI();
        getData();
    }

    private void initUI()
    {
        //toolbar
        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);
    }

    private void getData() {
        /*if(type == null)
        {
            firestore.collection("product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot doc : task.getResult())
                                {
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
        else if(!type.isEmpty() && type.equalsIgnoreCase(type))
        {
            firestore.collection("product").whereEqualTo("type",type)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot doc : task.getResult())
                                {
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }*/

        if (type == null) {
            database.getReference("product").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ShowAllModel showAllModel = dataSnapshot.getValue(ShowAllModel.class);
                        showAllModelList.add(showAllModel);
                        showAllAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Database", "Error reading data", error.toException());
                }
            });
        } else if (!type.isEmpty() && type.equalsIgnoreCase(type)) {
            Query query = database.getReference("product").orderByChild("type").equalTo(type);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ShowAllModel showAllModel = dataSnapshot.getValue(ShowAllModel.class);
                        showAllModelList.add(showAllModel);
                        showAllAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Database", "Error reading data", error.toException());
                }
            });
        }
    }
}