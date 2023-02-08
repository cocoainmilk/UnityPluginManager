using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;

public class Test : MonoBehaviour
{
    void Start()
    {
        var loginCmd =
#if UNITY_EDITOR
            new PluginCommandLoginGuestDummy(null);
#elif UNITY_ANDROID
            new PluginCommandLoginGoogle()
#elif UNITY_IOS
            new PluginCommandLoginApple(null)
#else
            new PluginCommandLoginGuestDummy(null);
#endif


        PluginManager.Instance.Request(loginCmd)
            .Subscribe(_ =>
            {
                if(loginCmd.Response.IsSuccess)
                {
                    Debug.Log($"Login Success. AccessToken({loginCmd.Response.AccessToken})");
                }
                else
                {
                    Debug.LogError("Login Failed.");
                }
            })
            .AddTo(this);
    }
}
