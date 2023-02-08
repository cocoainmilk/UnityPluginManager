//
//  MessagingPluginViewController.swift
//  UnityPluginApple
//
//  Created by  on 2020/08/07.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UnityPluginCore
import UserNotifications
import UIKit

class Plugin_messaging : PluginViewControllerBase
{
    override open func process() throws
    {
        print("apple messaging")
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onDeviceToken),
            name: Plugin.NotificationDeviceToken,
            object: nil)
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onDeviceTokenFail),
            name: Plugin.NotificationDeviceTokenFail,
            object: nil)
        
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            
            if error != nil
            {
                print(error.debugDescription)
                self.sendFailToUnity()
                return
            }
            
            if granted
            {
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
            else
            {
                self.sendResult(token: nil)
            }
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func onDeviceToken(notification: NSNotification)
    {
        let data = notification.userInfo!["deviceToken"]
        if data is NSData
        {
            let token = Data(referencing: data as! NSData).map { String(format: "%02.2hhx", $0) }.joined()
            sendResult(token: token)
        }
        else if data is Data
        {
            let token = (data as! Data).map { String(format: "%02.2hhx", $0) }.joined()
            sendResult(token: token)
        }
        else
        {
           sendFailToUnity()
        }
    }
    
    @objc func onDeviceTokenFail(notification: NSNotification)
    {
        sendFailToUnity()
    }
    
    func sendResult(token: String?)
    {
        var parameter: [String : Any] = [:]
        parameter["Token"] = token
        sendSuccessToUnity(parameter: parameter)
    }
}


