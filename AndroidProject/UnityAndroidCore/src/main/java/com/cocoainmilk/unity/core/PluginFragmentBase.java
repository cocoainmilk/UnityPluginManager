package com.cocoainmilk.unity.core;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;


import androidx.annotation.Nullable;

public abstract class PluginFragmentBase extends Fragment
{
    protected PluginBase pluginBase = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        try
        {
            int id = getArguments().getInt("Id");
            String parameterStr = getArguments().getString("Parameter");
            pluginBase = new PluginBase();
            pluginBase.init(getActivity(), id, parameterStr);
            process();
        }
        catch (Exception e)
        {
            log(e);
            sendFailToUnity();
        }
    }

    protected abstract void process() throws Exception;


    public void show(Activity activity)
    {
        activity.getFragmentManager().beginTransaction().add(0, this).commit();
    }

    public void hide()
    {
        try
        {
            getActivity().getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
        catch (Exception ex)
        {
        }
    }

    public String getParameterString(String name) throws Exception
    {
        return pluginBase.getParameterString(name);
    }

    public int getParameterInt(String name) throws Exception
    {
        return pluginBase.getParameterInt(name);
    }

    public boolean getParameterBoolean(String name) throws Exception
    {
        return pluginBase.getParameterBoolean(name);
    }

    public JSONArray getParameterArray(String name) throws Exception
    {
        return pluginBase.getParameterArray(name);
    }

    protected void sendSuccessToUnity(JSONObject result)
    {
        pluginBase.sendSuccessToUnity(result);
        hide();
    }

    protected void sendCancelToUnity()
    {
        pluginBase.sendCancelToUnity();
        hide();
    }

    protected void sendFailToUnity()
    {
        pluginBase.sendFailToUnity();
        hide();
    }

    protected  void sendPendingToUnity()
    {
        pluginBase.sendPendingToUnity();
        hide();
    }


    public Bundle getMetaData() throws Exception
    {
        return pluginBase.getMetaData();
    }

    public String getStringByName(String name)
    {
        return pluginBase.getStringByName(name);
    }

    protected static void log(String s)
    {
        PluginBase.log(s);
    }

    protected static void log(Exception e)
    {
        PluginBase.log(e);
    }
}
