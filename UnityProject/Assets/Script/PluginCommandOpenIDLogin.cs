using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;

public class PluginCommandLoginOpenID : PluginCommand
{
    [Serializable]
    public class RequestBody
    {
        // 애플
        public string AppleUserId;
    }

    [Serializable]
    public class ResponseBody : ResponseBodyBase
    {
        public string AccessToken;

        // 애플
        public string AppleUserId;
    }

    [PluginRequestBody] public RequestBody RequestInfo;
    [PluginResponseBody] public ResponseBody Response = new();

    public PluginCommandLoginOpenID(string packageName, string classNamePrefix)
        : base(packageName, classNamePrefix)
    {
        RequestInfo = new RequestBody();
    }
}

public class PluginCommandLogoutOpenID : PluginCommand
{
    public PluginCommandLogoutOpenID(string packageName, string classNamePrefix)
        : base(packageName, classNamePrefix)
    {
    }
}

public class PluginCommandLoginGuestDummy : PluginCommandLoginOpenID
{
    public PluginCommandLoginGuestDummy(string guestId) : base(null, null)
    {
        Response.SetSuccess();
        Response.AccessToken = guestId;
    }
}

public class PluginCommandLoginGoogle : PluginCommandLoginOpenID
{
    public PluginCommandLoginGoogle() : base("google", "login")
    {
    }
}

public class PluginCommandLogoutGoogle : PluginCommand
{
    public PluginCommandLogoutGoogle() : base("google", "logout")
    {
    }
}

public class PluginCommandLoginApple : PluginCommandLoginOpenID
{
    public PluginCommandLoginApple(string id) : base("apple", "login")
    {
        RequestInfo.AppleUserId = id;
    }
}

public class PluginCommandLogoutApple : PluginCommand
{
    public PluginCommandLogoutApple() : base("apple", "logout")
    {
    }
}
