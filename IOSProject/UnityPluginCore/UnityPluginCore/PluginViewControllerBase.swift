//
//  PluginViewControllerBase.swift
//  UnityPluginCore
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

open class PluginViewControllerBase : UIViewController
{
//    public enum DefaultError : Error
//    {
//        case Error
//    }
    
    var pluginBase : PluginBase
    
    public required init(pluginProtocol: PluginProtocol, id: Int, parameter: String?)
    {
        pluginBase = PluginBase(pluginProtocol: pluginProtocol, id: id, parameter: parameter)
        super.init(nibName: nil, bundle: nil)
    }
    
    public required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    open override func viewDidLoad()
    {
        do
        {
            try process()
        }
        catch
        {
            self.sendFailToUnity()
        }
    }

    open func process() throws
    {
        
    }
    
    func hide()
    {
        DispatchQueue.main.async {
            self.willMove(toParent: nil)
            self.view.removeFromSuperview()
            self.removeFromParent()
        }
    }
    
    public func getUnityViewController() -> UIViewController
    {
        return pluginBase.getUnityViewController()
    }
    
    public func getParameter<T>(key: String) -> T?
    {
        return pluginBase.getParameter(key: key)
    }
    
    public func sendSuccessToUnity(parameter: [String : Any?]?)
    {
        pluginBase.sendSuccessToUnity(parameter: parameter)
        hide()
    }
    
    public func sendFailToUnity()
    {
        pluginBase.sendFailToUnity()
        hide()
    }

    public func sendCancelToUnity()
    {
        pluginBase.sendCancelToUnity()
        hide()
    }

    public func sendPendingToUnity()
    {
        pluginBase.sendPendingToUnity()
        hide()
    }

    public func sendNoPermissionToUnity()
    {
        pluginBase.sendNoPermissionToUnity()
        hide()
    }
    
    public func sendPendingUntilAllowToUnity()
    {
        pluginBase.sendPendingUntilAllowToUnity()
        hide()
    }
}

