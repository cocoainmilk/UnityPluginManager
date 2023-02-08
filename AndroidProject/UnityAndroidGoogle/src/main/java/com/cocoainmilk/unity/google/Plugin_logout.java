package com.cocoainmilk.unity.google;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.cocoainmilk.unity.core.PluginBase;

public class Plugin_logout extends PluginBase
{
    @Override
    public void process() throws Exception
    {
        String webClientId = getStringByName("google_web_client_id");
        final GoogleSignInOptions signInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        final GoogleSignInClient signInClient = GoogleSignIn.getClient(activity, signInOption);
        signInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(Task<Void> task)
                    {
                        sendSuccessToUnity(null);
                    }
                });
    }
}
