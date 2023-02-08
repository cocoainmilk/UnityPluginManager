//
//  Plugin.swift
//  UnityPluginCore
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

@objc public class Plugin : NSObject
{
    static let shared = Plugin()
    
    // Notication
    public static let NotificationOpenURL = Notification.Name(rawValue: "kUnityOnOpenURL")
    public static let NotificationDeviceToken = Notification.Name(rawValue: "kUnityDidRegisterForRemoteNotificationsWithDeviceToken")
    public static let NotificationDeviceTokenFail = Notification.Name(rawValue: "kUnityDidFailToRegisterForRemoteNotificationsWithError")
    
    @objc public static func GetInstance() -> Plugin {
        return shared
    }
    
    @objc public func request(pluginProtocol: PluginProtocol, id: Int, packageName: String, className: String, parameter: String?)
    {
        DispatchQueue.main.async {
            self._request(pluginProtocol: pluginProtocol, id: id, packageName: packageName, className: className, parameter: parameter)
        }
    }
    
    @objc public func _request(pluginProtocol: PluginProtocol, id: Int, packageName: String, className: String, parameter: String?)
    {
        let bundleName = "UnityPlugin" + toUpperFirst(str: packageName)
        let className = bundleName + ".Plugin_" + className
        let classType : AnyClass? = NSClassFromString(className)
        
        if let viewControllerClass = classType as? PluginViewControllerBase.Type
        {
            let viewController = viewControllerClass.init(pluginProtocol: pluginProtocol, id: id, parameter:parameter)

            let parent = pluginProtocol.getUnityViewController()
            parent.addChild(viewController)
            parent.view.addSubview(viewController.view)
            viewController.didMove(toParent: parent)
        }
        else if let pureClass = classType as? PluginBase.Type
        {
            let pure = pureClass.init(pluginProtocol: pluginProtocol, id: id, parameter:parameter)
            do
            {
                try pure.process()
            }
            catch
            {
                let plugin = PluginBase(pluginProtocol: pluginProtocol, id: id, parameter: parameter)
                plugin.sendFailToUnity()
            }
        }
        else
        {
            print("plugin not found. " + packageName + "." + className)
            let plugin = PluginBase(pluginProtocol: pluginProtocol, id: id, parameter: parameter)
            plugin.sendFailToUnity()
        }
    }
    
    func toUpperFirst(str: String) -> String
    {
        return str.prefix(1).capitalized + str.dropFirst().lowercased()
    }
    
    @objc public static func notifyOpenUrl(url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool
    {
        let sourceApplication = options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String 
        let annotation = options[UIApplication.OpenURLOptionsKey.annotation]
        return notifyOpenUrl(url: url, sourceApplication: sourceApplication, annotation: annotation)
    }
    
    @objc public static func notifyOpenUrl(url: URL, sourceApplication: String?, annotation: Any?) -> Bool
    {
        var userInfo : [String: Any] = [:]
        userInfo["url"] = url;
        if sourceApplication != nil
        {
            userInfo["sourceApplication"] = sourceApplication;
        }
        if annotation != nil
        {
            userInfo["annotation"] = annotation;
        }

        NotificationCenter.default.post(name:Plugin.NotificationOpenURL, object: nil, userInfo: userInfo)
        
        return true
    }
    
    @objc public static func notifyDeviceToken(deviceToken: Data)
    {
        NotificationCenter.default.post(name:Plugin.NotificationDeviceToken, object: nil, userInfo: ["deviceToken":deviceToken])
    }
    
    @objc public static func notifyDeviceTokenFail(error: Error)
    {
        NotificationCenter.default.post(name:Plugin.NotificationDeviceTokenFail, object: nil, userInfo: nil)
    }
}
