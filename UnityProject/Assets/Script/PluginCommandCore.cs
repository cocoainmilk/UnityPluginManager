using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;

public class PluginCommandClipboard : PluginCommand
{
    [Serializable]
    public class RequestBody
    {
        public string PlainText;
    }

    [PluginRequestBody] public RequestBody RequestInfo;

    public PluginCommandClipboard(string plainText)
        : base("core", "clipboard")
    {
        RequestInfo = new RequestBody();
        RequestInfo.PlainText = plainText;
    }
}

public class PluginCommandShare : PluginCommand
{
    [Serializable]
    public class RequestBody
    {
        public string Title;
        public string PlainText;
    }

    [PluginRequestBody] public RequestBody RequestInfo;

    public PluginCommandShare(string title, string plainText)
        : base("core", "share")
    {
        RequestInfo = new RequestBody();
        RequestInfo.Title = title;
        RequestInfo.PlainText = plainText;
    }
}
