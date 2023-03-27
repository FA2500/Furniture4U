package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import my.fa250.furniture4u.R;
import my.fa250.furniture4u.com.HomePageActivity;

public class MultipleSignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    private static final int REQ_ONE_TAP = 2; //Google Auth

    //Google Auth
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_sign_in);
        mAuth = FirebaseAuth.getInstance();


        //Google
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        //.setServerClientId(getString(R.string.default_web_client_ids))
                        .setServerClientId("1052577895815-hfslo6oo69sv1d4mq15j4um6sljedhib.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("1052577895815-hfslo6oo69sv1d4mq15j4um6sljedhib.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(MultipleSignInActivity.this
                ,googleSignInOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("Auth","Already Log in");
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

    }

    private void SignInEmail()
    {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    //Protected

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","Result Code = "+requestCode);

        switch (requestCode) {
            case REQ_ONE_TAP:
                SignInClient oneTapClient = Identity.getSignInClient(this);
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