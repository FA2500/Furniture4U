package my.fa250.furniture4u.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.functions.HttpsCallableReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.model.AddressModel;
import my.fa250.furniture4u.model.CartModel;

public class PaymentActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("asia-southeast1");
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://furniture4u-93724-default-rtdb.asia-southeast1.firebasedatabase.app/");

    WebView webView;

    private Double total;
    private List<String> cardID;
    private String address;
    private String addressID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        total = intent.getDoubleExtra("total", 0.0);
        cardID = (List<String>) intent.getSerializableExtra("cardID");
        address = intent.getStringExtra("address");
        addressID = intent.getStringExtra("addressID");

        initUI();
        createBill();
    }

    private void initUI()
    {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void createBill()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("collection_id", "oqfedjph");
        data.put("email", (!UserInfo.getEmail().isEmpty()) ?  UserInfo.getEmail() : "mfarisammar@gmail.com");
        data.put("name", (!UserInfo.getName().isEmpty()) ?  UserInfo.getName() : "faris" );
        data.put("amount", (!total.isNaN()) ? (total*100) : 10000);
        data.put("description", "Furniture.");
        mFunctions.getHttpsCallable("createBill").call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Map<String, Object> result = (Map<String, Object>) httpsCallableResult.getData();
                        String billId = (String) result.get("id");
                        String redirectUrl = (String) result.get("url");
                        Log.d("Payment","successful");
                        Log.d("Payment",result.toString());
                        Log.d("Payment",billId);
                        Log.d("Payment",redirectUrl);

                        // Redirect the user to the payment page
                        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                        //startActivity(browserIntent);

                        //
                        webView.loadUrl(redirectUrl);
                        webView.setWebViewClient(new WebViewClient()
                        {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon)
                            {
                                super.onPageStarted(view, url, favicon);
                                Log.d("Webview","Current URL = "+url);
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);

                                if(url.contains("bills"))
                                {
                                    Log.d("GETBILLID",url.substring(38));
                                }
                                else if(url.contains("success"))
                                {
                                    for(int i = 0 ; i < cardID.size() ; i++)
                                    {
                                        Log.w("CARDi",cardID.get(i));
                                        int finalI = i;
                                        database.getReference("user/" + mAuth.getCurrentUser().getUid() + "/cart").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                                {
                                                    if(dataSnapshot.getKey().equals(cardID.get(finalI)))
                                                    {
                                                        Log.d("CARTMODEL","cart = "+dataSnapshot.getValue(CartModel.class).toString());
                                                        CartModel cart = dataSnapshot.getValue(CartModel.class);
                                                        database.getReference("user/" +mAuth.getCurrentUser().getUid() + "/order").child(cardID.get(finalI)).setValue(cart);
                                                        database.getReference("user/" +mAuth.getCurrentUser().getUid() + "/order").child(cardID.get(finalI)).child("address").setValue(addressID);
                                                        String orderID = database.getReference("order/").getKey();
                                                        HashMap<String,Object> a = new HashMap<String, Object>();
                                                        a.put("address",address);
                                                        a.put("addressID",addressID);
                                                        database.getReference("order/").child(orderID).setValue(cart);
                                                        database.getReference("order/").child(orderID).updateChildren(a);
                                                        //database.getReference("order/").push().child(mAuth.getCurrentUser().getUid()).child("addressID").setValue(addressID);
                                                        //database.getReference("order/").push().child(mAuth.getCurrentUser().getUid()).child("address").setValue(address);

                                                        database.getReference("user/" +mAuth.getCurrentUser().getUid() + "/cart").child(cardID.get(finalI)).removeValue();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.w("Database", "Error reading data", error.toException());
                                            }
                                        });
                                    }
                                    Log.d("GOTO","SUCCESS");
                                    Intent intent = new Intent(PaymentActivity.this, PaymentStatus.class);
                                    intent.putExtra("status","success");
                                    startActivity(intent);
                                    //nav( "True",url);
                                }
                                else if(url.contains("redirect?billplz[id]"))
                                {
                                    Log.d("GOTO","FAILED");
                                    Intent intent = new Intent(PaymentActivity.this, PaymentStatus.class);
                                    intent.putExtra("status","failed");
                                    startActivity(intent);
                                    //nav( "False",url);
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                            String message = ffe.getMessage();
                            Log.d("Payment",message);
                            Log.e("Payment", "createBill failed", e);
                        }
                    }});
    }

    private void CartToOrder()
    {

    }
}