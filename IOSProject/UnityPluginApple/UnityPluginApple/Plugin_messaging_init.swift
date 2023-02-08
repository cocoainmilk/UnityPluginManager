//
//  Messaging_initPluginViewController.swift
//  UnityPluginApple
//
//  Created by  on 2020/08/11.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UnityPluginCore
import UserNotifications
import UIKit

class MessagingObserver : NSObject
{
    deinit
    {
        NotificationCenter.default.removeObserver(self)
    }
    
    public func configure()
    {
        clear()
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.clear),
            name: UIApplication.didBecomeActiveNotification,
            object: nil)
    }
    
    @objc func clear()
    {
        UIApplication.shared.applicationIconBadgeNumber = 0;
        UNUserNotificationCenter.current().removeAllDeliveredNotifications()
    }
}

class Plugin_messaging_init : PluginBase
{
    static let observer = MessagingObserver()
    
    override open func process() throws
    {
        print("apple messaging init")
        Plugin_messaging_init.observer.configure()
        sendSuccessToUnity(parameter: nil)
    }
}
