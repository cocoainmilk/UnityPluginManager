package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import androidx.annotation.NonNull;

public class Plugin_billing_check_open extends BillingPluginFragmentBase
{
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult)
    {
        try
        {
            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK)
            {
                sendFailToUnity();
                return;
            }

            Purchase.PurchasesResult result = billingClient.queryPurchases(SkuType.INAPP);
            if (result.getResponseCode() != BillingClient.BillingResponseCode.OK)
            {
                sendFailToUnity();
                return;
            }

            if (result.getPurchasesList() == null || result.getPurchasesList().size() == 0)
            {
                sendSuccessToUnity(null);
            }
            else
            {
                sendPendingToUnity();
            }
        }
        catch (Exception ex)
        {
            log(ex);
            sendFailToUnity();
        }
    }
}
