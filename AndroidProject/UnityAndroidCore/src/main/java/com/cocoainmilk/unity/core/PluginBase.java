package com.cocoainmilk.unity.core;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONObject;

public class PluginBase
{
    public static String TAG = "Unity";

    public final static int STATUS_CODE_FAIL = 0;
    public final static int STATUS_CODE_SUCCESS = 1;
    public final static int STATUS_CODE_CANCEL = 2;
    public final static int STATUS_CODE_PENDING = 3;

    int id = -1;
    JSONObject paramter = null;
    protected Activity activity = null;

    public void init(Activity activity, int id, String parameterStr)
    {
        this.activity = activity;
        this.id = id;
        if (parameterStr != null && !parameterStr.isEmpty())
        {
            try
            {
                paramter = new JSONObject(parameterStr);
            }
            catch (Exception ex)
            {
                log(ex);
            }
        }
    }

    public void process() throws Exception
    {

    }

    public int getPluginId()
    {
        return id;
    }

    public String getParameterString(String name) throws Exception
    {
        return paramter.getString(name);
    }

    public int getParameterInt(String name) throws Exception
    {
        return paramter.getInt(name);
    }

    public boolean getParameterBoolean(String name) throws Exception
    {
        return paramter.getBoolean(name);
    }

    public JSONArray getParameterArray(String name) throws Exception
    {
        return paramter.getJSONArray(name);
    }

    public void sendSuccessToUnity(JSONObject result)
    {
        sendToUnity(STATUS_CODE_SUCCESS, result);
    }

    public void sendFailToUnity()
    {
        sendToUnity(STATUS_CODE_FAIL, null);
    }

    public void sendPendingToUnity()
    {
        sendToUnity(STATUS_CODE_PENDING, null);
    }

    public void sendCancelToUnity()
    {
        sendToUnity(STATUS_CODE_CANCEL, null);
    }

    public void sendToUnity(int statusCode, JSONObject result)
    {
        if (result == null)
        {
            result = new JSONObject();
        }

        try
        {
            result.put("Id", id);
            result.put("StatusCode", statusCode);
            String resultStr = result.toString();
            log(resultStr);

            if (!BuildConfig.DEBUG)
            {
                UnityPlayer.UnitySendMessage("PluginManager", "OnCompleted", resultStr);
            }
        }
        catch (Exception e)
        {
            log(e.toString());
        }
    }

    public static void sendSuccessToUnity(int id, JSONObject result)
    {
        PluginBase plugin = new PluginBase();
        plugin.init(null, id, null);
        plugin.sendSuccessToUnity(result);
    }

    public static void sendFailToUnity(int id)
    {
        PluginBase plugin = new PluginBase();
        plugin.init(null, id, null);
        plugin.sendFailToUnity();
    }

    public static void sendCancelToUnity(int id)
    {
        PluginBase plugin = new PluginBase();
        plugin.init(null, id, null);
        plugin.sendCancelToUnity();
    }

    public Bundle getMetaData() throws Exception
    {
        ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        return appInfo.metaData;
    }

    public String getStringByName(String name)
    {
        return activity.getResources().getString(activity.getResources().getIdentifier(name, "string",
                activity.getPackageName()));
    }

    public static void log(String s)
    {
        if (BuildConfig.DEBUG)
        {
            Log.d(TAG, s);
        }
    }

    public static void log(Exception e)
    {
        if (BuildConfig.DEBUG)
        {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    public static void log(String s, boolean debug)
    {
        if (debug)
        {
            Log.d(TAG, s);
        }
    }

    public static void log(Exception e, boolean debug)
    {
        if (debug)
        {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }
}
