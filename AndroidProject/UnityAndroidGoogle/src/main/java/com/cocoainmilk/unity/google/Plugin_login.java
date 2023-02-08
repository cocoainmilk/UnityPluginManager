package com.cocoainmilk.unity.google;

import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import androidx.annotation.NonNull;

import com.cocoainmilk.unity.core.PluginFragmentBase;

public class Plugin_login extends PluginFragmentBase
{
    final int RC_SIGN_IN = 1;

    @Override
    protected void process() throws Exception
    {
        log("google.login");

        String webClientId = getStringByName("google_web_client_id");
        final GoogleSignInOptions signInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        final GoogleSignInClient signInClient = GoogleSignIn.getClient(getActivity(), signInOption);

        signInClient
                .silentSignIn()
                .addOnCompleteListener(
                        getActivity(),
                        new OnCompleteListener<GoogleSignInAccount>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task)
                            {
                                if (task.isSuccessful())
                                {
                                    GoogleSignInAccount account = task.getResult();
                                    sendSuccess(account);
                                }
                                else if (task.isCanceled())
                                {
                                    sendCancelToUnity();
                                }
                                else
                                {
                                    getActivity().runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Intent intent = signInClient.getSignInIntent();
                                            startActivityForResult(intent, RC_SIGN_IN);
                                        }
                                    });
                                }
                            }
                        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                sendSuccess(result.getSignInAccount());
            }
            else if (result.getStatus().getStatusCode() == 12501)
            {
                sendCancelToUnity();
            }
            else
            {
                sendFailToUnity();
            }
        }
    }

    void sendSuccess(GoogleSignInAccount account)
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("AccessToken", account.getIdToken());
            sendSuccessToUnity(json);
        }
        catch (Exception e)
        {
            sendFailToUnity();
        }
    }
}
