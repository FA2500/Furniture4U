package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.UserInfo;
import my.fa250.furniture4u.com.HomePageActivity;

public class MultipleSignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    private static final int REQ_ONE_TAP = 2; //Google Auth

    //Google Auth
    private BeginSignInRequest signInRequest;

    //Facebook
    CallbackManager callbackManager;

    //Twitter
    OAuthProvider.Builder provider;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_sign_in);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        //Google
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        //.setServerClientId(getString(R.string.default_web_client_ids))
                        .setServerClientId("1052577895815-3asgokttqccl3curjrk41v8s45sl01ru.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("1052577895815-3asgokttqccl3curjrk41v8s45sl01ru.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(MultipleSignInActivity.this
                ,googleSignInOptions);

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.w("FB","ERROR ",exception);
                        // App code
                    }
                });

        initUI();
    }

    private void initUI()
    {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile","user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB", "SUCCESS FACEBOOK LOGIN");
                handleFacebookAccessToken(loginResult.getAccessToken());
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.w("FB","ERROR",exception);
                // App code
            }
        });
        loginButton.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        provider = OAuthProvider.newBuilder("twitter.com");
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("Auth","Already Log in with"+currentUser.getDisplayName());
            getUserData((currentUser));
            Intent intent = new Intent(MultipleSignInActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    //Public Method

    public void BtnSignInFacebook(View v)
    {
        Log.d("Auth","Login with Facebook");
        SignInFacebook();
    }

    public void BtnSignInGoogle(View v)
    {
        Log.d("Auth","Login with Google");
        SignInGoogle();
    }

    public void BtnSignInTwitter(View v)
    {
        Log.d("Auth","Login with Twitter");
        SignInTwitter();
    }

    public void BtnSignInEmail(View v)
    {
        Log.d("Auth","Login with Email");
        SignInEmail();
    }

    //Private Method

    private void SignInFacebook()
    {

    }

    private void SignInGoogle()
    {
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,REQ_ONE_TAP);
    }

    private void SignInTwitter()
    {
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    sendUserData(user,"Twitter");
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.w("TWIT","PENDING ERR",e);
                                    Toast.makeText(MultipleSignInActivity.this, "Failed Pending", Toast.LENGTH_SHORT).show();
                                }
                            });
        } else {
            mAuth.startActivityForSignInWithProvider(this,provider.build())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //get ID
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendUserData(user,"Twitter");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed
                            Log.w("TWIT","LOGIN ERR",e);
                            Toast.makeText(MultipleSignInActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void SignInEmail()
    {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    private void sendUserData(FirebaseUser user, String provider)
    {
        UserInfo.setName(user.getDisplayName());
        UserInfo.setEmail(user.getEmail());
        UserInfo.setPhone(user.getPhoneNumber());
        UserInfo.setRole("Customer");
        UserInfo.setProvider(provider);

        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("name", user.getDisplayName());
        userinfo.put("email", user.getEmail());
        userinfo.put("phone",user.getPhoneNumber());
        userinfo.put("role", "Customer");
        userinfo.put("provider", provider);

        db.collection("user").document(user.getUid())
                .set(userinfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Database", "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(MultipleSignInActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database", "Error writing document", e);
                    }
                });
    }

    private void getUserData(FirebaseUser user)
    {
        db.collection("user").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            UserInfo.setName(doc.get("name").toString());
                            UserInfo.setEmail(doc.get("email").toString());
                            UserInfo.setPhone(doc.get("phone").toString());
                            UserInfo.setRole("Customer");
                            UserInfo.setProvider(doc.get("provider").toString());
                            Log.d("Database", "DocumentSnapshot successfully read!");
                            Intent intent = new Intent(MultipleSignInActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database", "Error read document", e);
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FB", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendUserData(user,"Facebook");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FB", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MultipleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //Protected

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","Result Code = "+requestCode);

        switch (requestCode) {
            case REQ_ONE_TAP:
                SignInClient oneTapClient = Identity.getSignInClient(MultipleSignInActivity.this);
                Log.d("Google Auth","One Tap Client = "+data.getData());
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    Log.d("Google Auth","ID Token = "+idToken);
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("Google Auth", "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            sendUserData(user,"Google");
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("Google Auth", "signInWithCredential:failure", task.getException());
                                        }
                                    }
                                });
                        Log.d("Google Auth", "Got ID token.");
                    }
                } catch (ApiException e) {
                    Log.d("Google Auth", "Missing ID token. "+e.toString());
                }
                break;
        }
    }

}