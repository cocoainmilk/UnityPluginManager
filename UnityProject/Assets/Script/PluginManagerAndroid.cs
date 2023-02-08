using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PluginManagerAndroid : SingletonMonoBehaviourStatic<PluginManagerAndroid>, IPluginImplement
{
    static AndroidJavaObject mainActivity;
    static AndroidJavaClass pluginCoreClass;

    protected override void Awake()
    {
        base.Awake();
        AndroidJavaClass UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        mainActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        pluginCoreClass = new AndroidJavaClass("com.cocoainmilk.unity.core.Plugin");
    }

    public bool Request(int id, string packageName, string classPrefixName, string parameter)
    {
        mainActivity.Call(
            "runOnUiThread",
            new AndroidJavaRunnable(() =>
                {
                    pluginCoreClass.CallStatic("request", id, packageName, classPrefixName, parameter);
                }
            )
        );

        return true;
    }
}
