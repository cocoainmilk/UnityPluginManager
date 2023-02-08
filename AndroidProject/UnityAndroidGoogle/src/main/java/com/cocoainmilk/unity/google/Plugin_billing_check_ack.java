package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Plugin_billing_check_ack extends BillingPluginFragmentBase
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
                return;
            }

            Purchase purchase = result.getPurchasesList().get(0);
            if (purchase.isAcknowledged())
            {
                sendSuccessToUnity(null);
                return;
            }

            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
            {
                try
                {
                    JSONObject json = new JSONObject();
                    json.put("Receipt", purchase.getOriginalJson());
                    json.put("Signature", purchase.getSignature());
                    json.put("PurchaseToken", purchase.getPurchaseToken());
                    json.put("ProductId", purchase.getSku());
                    sendSuccessToUnity(json);
                }
                catch (Exception ex)
                {
                    sendFailToUnity();
                }
            }
            else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
            {
                sendFailToUnity();
            }
        }
        catch (Exception ex)
        {
            sendFailToUnity();
        }
    }
}