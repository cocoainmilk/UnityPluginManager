package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import androidx.annotation.NonNull;

public class Plugin_billing_finish_all extends BillingPluginFragmentBase implements  ConsumeResponseListener
{
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult)
    {
        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
        {
            Purchase.PurchasesResult result = billingClient.queryPurchases(SkuType.INAPP);
            if(result.getResponseCode() == BillingClient.BillingResponseCode.OK)
            {
                for(Purchase purchase : result.getPurchasesList())
                {
                    if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged())
                    {
                        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();

                        billingClient.consumeAsync(consumeParams, this);
                    }
                }
            }
        }
    }

    @Override
    public void onConsumeResponse(BillingResult billingResult, String purchaseToken)
    {
        log("dd");
    }
}
