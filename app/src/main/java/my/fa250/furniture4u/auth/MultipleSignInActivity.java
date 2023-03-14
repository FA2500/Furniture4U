package my.fa250.furniture4u.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.furniture4u.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
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

public class MultipleSignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int REQ_ONE_TAP = 2; //Google Auth

    //Google Auth
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_sign_in);
        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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

    //Private Method

    private void SignInFacebook()
    {

    }

    private void SignInGoogle()
    {
        try {
            signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            // Your server's client ID, not your Android client ID.
                            .setServerClientId(getString(R.string.default_web_client_ids))
                            //.setServerClientId("1052577895815-36geogs6iqr70c8ng2ngh5qes42k8m45.apps.googleusercontent.com")
                            // Only show accounts previously used to sign in.
                            .setFilterByAuthorizedAccounts(true)
                            .build())
                    .build();

            /*oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            try {
                                startActivityForResult(getIntent(),REQ_ONE_TAP);
                            } catch (Exception e) {
                                Log.e("Auth", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // No saved credentials found. Launch the One Tap sign-up flow, or
                            // do nothing and continue presenting the signed-out UI.
                            Log.d("Auth", e.getLocalizedMessage());
                        }
                    });*/
        }
        catch (Exception e)
        {
            Log.d("Auth","Wrong ID "+e.toString() );
        }

    }

    private void SignInTwitter()
    {

    }

    //Protected

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","Result Code = "+resultCode);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
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
                    // ...
                }
                break;
        }
    }

}