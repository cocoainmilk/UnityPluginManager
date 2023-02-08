package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import androidx.annotation.NonNull;

public class Plugin_billing_finish extends BillingPluginFragmentBase implements ConsumeResponseListener
{
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult)
    {
        try
        {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
            {
                String token = getParameterString("PurchaseToken");
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(token)
                        .build();
                billingClient.consumeAsync(consumeParams, this);
            }

            else
            {
                sendFailToUnity();
            }
        }
        catch (Exception ex)
        {
            log(ex);
            sendFailToUnity();
        }

    }

    @Override
    public void onConsumeResponse(BillingResult billingResult, String purchaseToken)
    {
        if (billingResult.getResponseCode() == BillingResponseCode.OK)
        {
            sendSuccessToUnity(null);
        }
        else
        {
            sendFailToUnity();
        }
    }
}
