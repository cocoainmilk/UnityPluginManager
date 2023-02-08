using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using UnityEngine;
using UniRx;

public interface IPluginImplement
{
    bool Request(int id, string packageName, string classPrefixName, string parameter);
}

public class PluginManager : SingletonMonoBehaviourStatic<PluginManager>
{
    IPluginImplement impletement = null;
    Dictionary<int, PluginCommand> pendingCommands = new();
    int uniqueID = 0;

    protected override void Awake()
    {
        base.Awake();
#if UNITY_EDITOR
        impletement = PluginManagerEditor.Instance;
#elif UNITY_ANDROID
        impletement = PluginManagerAndroid.Instance;
#elif UNITY_IOS
        impletement = PluginManagerIOS.Instance;
#else
        impletement = PluginManagerEditor.Instance;
#endif
    }

    public IObservable<PluginCommand> Request(PluginCommand command)
    {
        return Observable.ReturnUnit()
            .ContinueWith(_ =>
            {
                // 요청 파라미터를 JSON으로 변환
                string request = null;
                foreach(FieldInfo field in command.GetType().GetFields())
                {
                    if(field.IsDefined(typeof(PluginRequestBodyAttribute), false))
                    {
                        request = JsonUtility.ToJson(field.GetValue(command));
                        break;
                    }
                }

                // ID값으로 OnCompleted()에서 응답을 식별함
                int id = Register(command);

                Debug.Log($"PluginManager.Request : {id} {command.PackageName} {command.ClassPrefixName} {request}");

                if(!impletement.Request(id, command.PackageName, command.ClassPrefixName, request))
                {
                    OnCompleted($"{{\"Id\":{id}, \"StatusCode\":0}}");
                }

                return command.OnCompletedAsObservable();
            });
    }

    // 네이티브 플러그인에서 호출됨
    void OnCompleted(string json)
    {
        Debug.Log($"PluginManager.OnCompleted : {json}");

        var responceBase = JsonUtility.FromJson<PluginCommand.ResponseBodyBase>(json);
        if(pendingCommands.TryGetValue(responceBase.Id, out var command))
        {
            // JSON을 응답으로 변환
            FieldInfo fieldInfo = null;
            foreach(FieldInfo field in command.GetType().GetFields())
            {
                if(field.IsDefined(typeof(PluginResponseBodyAttribute), false))
                {
                    fieldInfo = field;
                    break;
                }
            }

            if(fieldInfo != null)
            {
                object value = JsonUtility.FromJson(json, fieldInfo.FieldType);
                fieldInfo.SetValue(command, value);
            }

            Remove(responceBase.Id);

            // 완료 알림
            command.OnCompleted();
        }
    }

    int Register(PluginCommand command)
    {
        pendingCommands[uniqueID] = command;
        return uniqueID++;
    }

    void Remove(int id)
    {
        pendingCommands.Remove(id);
    }
}
