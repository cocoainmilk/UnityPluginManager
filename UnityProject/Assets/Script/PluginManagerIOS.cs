using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using UnityEngine;

public class PluginManagerIOS : SingletonMonoBehaviourStatic<PluginManagerIOS>, IPluginImplement
{
#if UNITY_IOS
    [DllImport("__Internal")]
    private static extern void RequestBridge(int id, string packageName, string className, string parameter);
#endif
    public bool Request(int id, string packageName, string classPrefixName, string parameter)
    {
#if UNITY_IOS
        RequestBridge(id, packageName, classPrefixName, parameter);
        return true;
#else
        return false;
#endif
    }
}
