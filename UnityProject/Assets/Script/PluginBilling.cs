using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;


public enum StoreType
{
    Test = 0,
    Google = 1,
    Apple = 2,

    Invalid,
}

public static class PluginBilling
{
    static bool Lock { get; set; } = false;

    public static StoreType GetStoreType(bool test = false)
    {
        if(test)
        {
            return StoreType.Test;
        }

#if UNITY_ANDROID
        return StoreType.Google;
#elif UNITY_IOS
        return StoreType.Apple;
#else
        return StoreType.Invalid;
#endif
    }

    static string GetPackageName()
    {
        switch(GetStoreType())
        {
            case StoreType.Google:
                return "google";
            case StoreType.Apple:
                return "apple";
            default:
                return string.Empty;
        }
    }

    public static bool AquireLock()
    {
        if(Lock)
        {
            return false;
        }
        else
        {
            Lock = true;
            return true;
        }
    }

    public static void ReleaseLock()
    {
        Lock = false;
    }

    public static IObservable<PluginCommandBillingInit> Init()
    {
        return new PluginCommandBillingInit(GetPackageName())
            .Request()
            .Select(protocol => (PluginCommandBillingInit)protocol);

    }

    public static IObservable<PluginCommandBillingDetail> Detail(string[] productId)
    {
        return new PluginCommandBillingDetail(GetPackageName(), productId)
            .Request()
            .Select(protocol => (PluginCommandBillingDetail)protocol);

    }

    public static IObservable<PluginCommandBillingBuy> Buy(string productId, bool test)
    {
        if(test)
        {
            var plugin = new PluginCommandBillingBuy(GetPackageName(), productId);
            plugin.Response.SetSuccess();
            return Observable.Return(plugin);
        }
        else
        {
            return new PluginCommandBillingBuy(GetPackageName(), productId)
                .Request()
                .Select(protocol => (PluginCommandBillingBuy)protocol);
        }
    }

    public static IObservable<PluginCommandBillingFinish> Finish(string purchaseToken)
    {
        return new PluginCommandBillingFinish(GetPackageName(), purchaseToken)
            .Request()
            .Select(protocol => (PluginCommandBillingFinish)protocol);
    }

    public static IObservable<PluginCommandBillingCheckAck> CheckAck()
    {
        return new PluginCommandBillingCheckAck(GetPackageName())
            .Request()
            .Select(protocol => (PluginCommandBillingCheckAck)protocol);
    }

    public static IObservable<PluginCommandBillingCheckOpen> CheckOpen(bool test)
    {
        if(test)
        {
            var plugin = new PluginCommandBillingCheckOpen(GetPackageName());
            plugin.Response.SetSuccess();
            return Observable.Return(plugin);
        }
        else
        {
            return new PluginCommandBillingCheckOpen(GetPackageName())
                .Request()
                .Select(protocol => (PluginCommandBillingCheckOpen)protocol);

        }
    }
}
