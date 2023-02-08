using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PluginManagerEditor : SingletonMonoBehaviourStatic<PluginManagerEditor>, IPluginImplement
{
    public bool Request(int id, string packageName, string classPrefixName, string parameter)
    {
        return false;
    }
}
