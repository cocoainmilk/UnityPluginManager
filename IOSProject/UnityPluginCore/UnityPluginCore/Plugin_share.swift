//
//  Plugin_share.swift
//  UnityPluginCore
//
//  Created by  on 2021/03/16.
//  Copyright © 2021 Company. All rights reserved.
//

import Foundation
import UIKit

public class Plugin_share : PluginBase
{
    public override func process() throws
    {
        let text : String? = getParameter(key: "PlainText")

        let activityItems = [text!]
        let activityViewController = UIActivityViewController(activityItems: activityItems, applicationActivities: nil)
        activityViewController.excludedActivityTypes = [UIActivity.ActivityType.postToFacebook]
        getUnityViewController().present(activityViewController, animated: true, completion:nil)
        sendSuccessToUnity(parameter: nil)
    }
}
