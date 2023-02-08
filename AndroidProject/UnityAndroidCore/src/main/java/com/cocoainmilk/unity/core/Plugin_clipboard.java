package com.cocoainmilk.unity.core;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Plugin_clipboard extends PluginBase
{
    @Override
    public void process() throws Exception
    {
        String plainText = getParameterString("PlainText");
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", plainText);
        clipboard.setPrimaryClip(clip);
        sendSuccessToUnity(null);
    }
}
