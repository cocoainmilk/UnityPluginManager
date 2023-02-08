package com.cocoainmilk.unity.google;

import com.android.billingclient.api.*;
import com.android.billingclient.api.BillingClient.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Plugin_billing_detail extends BillingPluginFragmentBase implements SkuDetailsResponseListener
{
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult)
    {
        try
        {
            if (billingResult.getResponseCode() == BillingResponseCode.OK)
            {
                JSONArray productId = getParameterArray("ProductId");
                List<String> skuList = new ArrayList<>();
                for (int i = 0; i < productId.length(); ++i)
                {
                    skuList.add(productId.getString(i));
                }
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
            if (billingResult.getResponseCode() == BillingResponseCode.OK)
            {
                JSONObject json = new JSONObject();
                JSONArray array = new JSONArray();
                for (SkuDetails sku : skuDetailsList)
                {
                    JSONObject detail = new JSONObject();
                    detail.put("ProductId", sku.getSku());
                    detail.put("Price", sku.getPrice());
                    detail.put("PriceValue", (float) (sku.getPriceAmountMicros() / 1000000d));
                    detail.put("CurrencyCode", sku.getPriceCurrencyCode());
                    array.put(detail);
                }
                json.put("Detail", array);

                sendSuccessToUnity(json);

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
