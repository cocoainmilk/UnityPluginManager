package com.cocoainmilk.unity.google;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.cocoainmilk.unity.core.PluginFragmentBase;

public abstract class BillingPluginFragmentBase extends PluginFragmentBase implements BillingClientStateListener, PurchasesUpdatedListener
{
    protected BillingClient billingClient;

    @Override
    protected void process() throws Exception {
        billingClient = BillingClient.newBuilder(getActivity())
                .setListener(this)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(this);
    }

    @Override
    public void onBillingServiceDisconnected() {
        sendFailToUnity();
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) { }
}
