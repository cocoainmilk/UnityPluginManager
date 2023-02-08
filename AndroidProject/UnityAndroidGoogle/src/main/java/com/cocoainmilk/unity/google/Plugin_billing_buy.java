package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Plugin_billing_buy extends BillingPluginFragmentBase implements SkuDetailsResponseListener
{
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult)
    {
        try
        {
            if (billingResult.getResponseCode() == BillingResponseCode.OK)
            {
                String productId = getParameterString("ProductId");
                List<String> skuList = new ArrayList<>();
                skuList.add(productId);
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(), this);
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
    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList)
    {
        try
        {
            if (billingResult.getResponseCode() == BillingResponseCode.OK && skuDetailsList.size() == 1)
            {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(0))
                        .build();
                BillingResult result = billingClient.launchBillingFlow(getActivity(), billingFlowParams);
                if (result.getResponseCode() != BillingResponseCode.OK)
                {
                    sendFailToUnity();
                }
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
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list)
    {
        try
        {
            if (billingResult.getResponseCode() == BillingResponseCode.OK)
            {
                Purchase purchase = list.get(0);
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
                {
                    JSONObject json = new JSONObject();
                    json.put("Receipt", purchase.getOriginalJson());
                    json.put("Signature", purchase.getSignature());
                    json.put("PurchaseToken", purchase.getPurchaseToken());
                    sendSuccessToUnity(json);
                }
                else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
                {
                    sendPendingToUnity();
                }
                else
                {
                    sendFailToUnity();
                }
            }
            else if (billingResult.getResponseCode() == BillingResponseCode.USER_CANCELED)
            {
                sendCancelToUnity();
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
}
