package com.cocoainmilk.unity.core;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Plugin_share extends PluginBase
{
    @Override
    public void process() throws Exception
    {
        String title = getParameterString("Title");
        String plainText = getParameterString("PlainText");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, plainText);
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            String[] excludePackageName = {"com.facebook.katana"};
            List<ResolveInfo> resolveInfo = activity.getPackageManager().queryIntentActivities(intent, 0);
            List<ComponentName> exclude = new ArrayList<>();
            for (String packageName : excludePackageName)
            {
                for (ResolveInfo info : resolveInfo)
                {
                    if (info.activityInfo.packageName.equals(packageName))
                    {
                        exclude.add(new ComponentName(packageName, info.activityInfo.name));
                    }
                }
            }
            if (!exclude.isEmpty())
            {
                shareIntent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, exclude.toArray(new Parcelable[exclude.size()]));
            }
        }

        activity.startActivity(shareIntent);

//        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);
//        List<Intent> targetedShareIntents = new ArrayList<>();
//        for (ResolveInfo resolveInfo : resInfo)
//        {
//            String packageName = resolveInfo.activityInfo.packageName;
//
//            if (!packageName.contains("com.facebook.katana"))
//            {
//                Intent targetedShareIntent = new Intent(intent);
//                targetedShareIntent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
//                targetedShareIntent.setPackage(packageName);
//                targetedShareIntents.add(targetedShareIntent);
//            }
//        }
//
//        Intent chooserIntent = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            // create chooser with empty intent in M could fix the empty cells problem
//            chooserIntent = Intent.createChooser(new Intent(), plainText);
//        }
//        else
//        {
//            chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), plainText);
//        }
//
//        if (chooserIntent != null)
//        {
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
//            activity.startActivity(chooserIntent);
//        }
//
//        sendSuccessToUnity(null);
    }
}
