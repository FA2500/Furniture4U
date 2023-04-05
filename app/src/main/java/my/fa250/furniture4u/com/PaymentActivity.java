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
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.functions.HttpsCallableReference;

import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;

public class PaymentActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
        data.put("email", "mfarisammar@gmail.com");
        data.put("name", "faris");
        data.put("amount", 10000);
        data.put("description", "Sofa.");
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
                                    Log.d("GOTO","SUCCESS");
                                    Intent intent = new Intent(PaymentActivity.this, PaymentStatus.class);
                                    startActivity(intent);
                                    //nav( "True",url);
                                }
                                else if(url.contains("failed"))
                                {
                                    Log.d("GOTO","FAILED");
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
}