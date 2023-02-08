//
//  DummyPluginProtocol.swift
//  TestNavinyang
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

@objc open class DummyPluginProtocol : NSObject, PluginProtocol
{
    var viewController : UIViewController
    
    @objc public init(viewController : UIViewController)
    {
        self.viewController = viewController
    }
    
    public func getUnityViewController() -> UIViewController
    {
        return self.viewController
    }
    
    public func onComleted(message: String) {
        
    }
}
