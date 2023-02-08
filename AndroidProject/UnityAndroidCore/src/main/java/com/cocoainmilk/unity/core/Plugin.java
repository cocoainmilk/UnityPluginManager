package com.cocoainmilk.unity.core;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

import android.os.Bundle;

public class Plugin
{
    // From Unity
    public static void request(int id, String packageName, String className, String parameter)
    {
        request(UnityPlayer.currentActivity, id, packageName, className, parameter);
    }

    public static void request(final Activity activity, final int id, final String packageName, final String className, final String parameter)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    _request(activity, id, packageName, className, parameter);
                }
                catch (Exception e)
                {
                    PluginBase.log(e);
                    PluginBase.sendFailToUnity(id);
                }
            }
        });
    }

    static void _request(Activity activity, int id, String packageName, String className, String parameter) throws Exception
    {
        try
        {
            PluginBase.log(String.format("%d %s %s %s", id, packageName, className, parameter));

            // packageName, className으로 동적으로 플러그인을 찾는다.

            String pluginName = String.format("com.cocoainmilk.unity.%s.Plugin_%s", packageName, className);
            Class pluginClass = Class.forName(pluginName);
            Object pluginObject = pluginClass != null ? pluginClass.getConstructor().newInstance() : null;

            if (pluginObject != null && pluginObject instanceof PluginFragmentBase)
            {
                PluginFragmentBase fragment = (PluginFragmentBase) pluginObject;
                Bundle bundle = new Bundle();
                bundle.putInt("Id", id);
                bundle.putString("Parameter", parameter);
                fragment.setArguments(bundle);
                fragment.show(activity);
            }
            else if (pluginObject != null && pluginObject instanceof PluginBase)
            {
                PluginBase plugin = (PluginBase) pluginObject;
                plugin.init(activity, id, parameter);
                plugin.process();
            }
            else
            {
                PluginBase.log("plugin not found. " + packageName + "." + className);
                PluginBase.sendFailToUnity(id);
            }
        }
        catch (Exception e)
        {
            PluginBase.log(e);
            PluginBase.sendFailToUnity(id);
        }
    }
}
