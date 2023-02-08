//
//  PluginBase.swift
//  UnityPluginCore
//
//  Created by  on 2020/07/30.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

open class PluginBase : NSObject
{
    static var STATUS_CODE_FAIL = 0
    static var STATUS_CODE_SUCCESS = 1
    static var STATUS_CODE_CANCEL = 2
    static var STATUS_CODE_PENDING = 3
    static var STATUS_CODE_NO_PERMISSION = 4
    static var STATUS_CODE_PENDING_UNTIL_ALLOW = 5
    
    var pluginProtocol: PluginProtocol
    var id: Int
    var parameter: [String: Any]? = nil
    
    public required init(pluginProtocol: PluginProtocol, id: Int, parameter: String?)
    {
        self.pluginProtocol = pluginProtocol
        self.id = id
        
        if let data = parameter?.data(using: .utf8)
        {
            self.parameter = try! JSONSerialization.jsonObject(with: data, options: []) as! [String: Any]
        }
    }
    
    open func process() throws
    {
        
    }
    
    public func getUnityViewController() -> UIViewController
    {
        return pluginProtocol.getUnityViewController()
    }
    
    public func getParameter<T>(key: String) -> T?
    {
        return parameter?[key] as? T
    }
    
    public func sendSuccessToUnity(parameter: [String : Any?]?)
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_SUCCESS, parameter: parameter)
    }
    
    public func sendFailToUnity()
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_FAIL, parameter: nil)
    }
    
    public func sendCancelToUnity()
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_CANCEL, parameter: nil)
    }
    
    public func sendPendingToUnity()
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_PENDING, parameter: nil)
    }
    
    public func sendNoPermissionToUnity()
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_NO_PERMISSION, parameter: nil)
    }
    
    public func sendPendingUntilAllowToUnity()
    {
        sendToUnity(statusCode: PluginBase.STATUS_CODE_PENDING_UNTIL_ALLOW, parameter: nil)
    }
    
    func sendToUnity(statusCode: Int, parameter: [String : Any?]?)
    {
        do
        {
            var result : [String : Any?] = parameter ?? [:]
            result["Id"] = id
            result["StatusCode"] = statusCode

            let data = try JSONSerialization.data(withJSONObject: result)
            let message = String(data: data, encoding: String.Encoding.utf8)!
            print(message)
            pluginProtocol.onComleted(message: message)
        }
        catch
        {
            sendFailToUnity()
        }
    }
}
