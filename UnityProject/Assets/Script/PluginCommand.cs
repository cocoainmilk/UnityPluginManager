using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UniRx;

[AttributeUsage(AttributeTargets.Field)]
public class PluginRequestBodyAttribute : Attribute
{
}

[AttributeUsage(AttributeTargets.Field)]
public class PluginResponseBodyAttribute : Attribute
{
}

public abstract class PluginCommand
{
    [Serializable]
    public class ResponseBodyBase
    {
        public int Id;
        public int StatusCode;
        public bool IsSuccess => StatusCode == 1;
        public bool IsCancel => StatusCode == 2;
        public bool IsPending => StatusCode == 3;
        public bool IsNoPermission => StatusCode == 4;
        public bool IsPendingUntilAllow => StatusCode == 5;

        public void SetSuccess()
        {
            StatusCode = 1;
        }

        public void SetCancel()
        {
            StatusCode = 2;
        }

        public void SetError()
        {
            StatusCode = 0;
        }
    }

    public string PackageName { get; protected set; }
    public string ClassPrefixName { get; protected set; }

    ReactiveProperty<PluginCommand> completed = new();
    public IObservable<PluginCommand> OnCompletedAsObservable() => completed.Where(command => command != null).Take(1);

    public PluginCommand(string packageName, string classNamePrefix)
    {
        PackageName = packageName;
        ClassPrefixName = classNamePrefix;
    }

    public void OnCompleted()
    {
        completed.Value = this;
    }

    public IObservable<PluginCommand> Request()
    {
        return PluginManager.Instance.Request(this);
    }
}
