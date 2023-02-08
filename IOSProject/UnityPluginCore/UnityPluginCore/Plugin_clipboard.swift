//
//  Plugin_clipboard.swift
//  UnityPluginCore
//
//  Created by  on 2020/09/17.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

public class Plugin_clipboard : PluginBase
{
    public override func process() throws
    {
        let text : String? = getParameter(key: "PlainText")
        if text != nil
        {
           UIPasteboard.general.string = text;
        }
        sendSuccessToUnity(parameter: nil)
    }
}
