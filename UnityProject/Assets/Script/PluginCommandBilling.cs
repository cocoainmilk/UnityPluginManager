using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;

public class PluginCommandBillingInit : PluginCommand
{
    public PluginCommandBillingInit(string packageName)
        : base(packageName, "billing_init")
    {
    }
}

public class PluginCommandBillingDetail : PluginCommand
{
    [Serializable]
    public class RequestBody
    {
        public string[] ProductId;
    }

    [Serializable]
    public class Detail
    {
        public string ProductId;
        public string Price;
        public float PriceValue;
        public string CurrencyCode;
    }

    [Serializable]
    public class ResponseBody : ResponseBodyBase
    {
        public Detail[] Detail;
    }

    [PluginRequestBody] public RequestBody RequestInfo;
    [PluginResponseBody] public ResponseBody Response = new();

    public PluginCommandBillingDetail(string packageName, string[] productId)
        : base(packageName, "billing_detail")
    {
        RequestInfo = new RequestBody();
        RequestInfo.ProductId = productId;
    }
}

public class PluginCommandBillingBuy : PluginCommand
{
    [SerializeField]
    public class RequestBody
    {
        public string ProductId;
    }

    [Serializable]
    public class ResponseBody : ResponseBodyBase
    {
        public string Receipt;
        public string Signature;        // android
        public string PurchaseToken;    // android. io : transition_id
    }

    [PluginRequestBody] public RequestBody RequestInfo;
    [PluginResponseBody] public ResponseBody Response = new();

    public PluginCommandBillingBuy(string packageName, string productId)
        : base(packageName, "billing_buy")
    {
        RequestInfo = new RequestBody();
        RequestInfo.ProductId = productId;
    }
}

public class PluginCommandBillingFinish : PluginCommand
{
    [SerializeField]
    public class RequestBody
    {
        public string PurchaseToken;
    }

    [PluginRequestBody] public RequestBody RequestInfo;

    public PluginCommandBillingFinish(string packageName, string purchaseToken)
        : base(packageName, "billing_finish")
    {
        RequestInfo = new RequestBody();
        RequestInfo.PurchaseToken = purchaseToken;
    }
}

public class PluginCommandBillingCheckAck : PluginCommand
{
    [Serializable]
    public class ResponseBody : ResponseBodyBase
    {
        public string Receipt;
        public string Signature;
        public string PurchaseToken;
        public string ProductId;
    }

    [PluginResponseBody] public ResponseBody Response = new();

    public PluginCommandBillingCheckAck(string packageName)
        : base(packageName, "billing_check_ack")
    {
    }
}

public class PluginCommandBillingCheckOpen : PluginCommand
{
    public class ResponseBody : ResponseBodyBase
    {
    }

    [PluginResponseBody] public ResponseBody Response = new();

    public PluginCommandBillingCheckOpen(string packageName)
        : base(packageName, "billing_check_open")
    {
    }
}
