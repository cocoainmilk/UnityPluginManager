//
//  Bridge.swift
//  UnityPluginCore
//
//  Created by  on 2020/01/04.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UIKit

@objc public protocol PluginProtocol : class
{
    @objc func getUnityViewController() -> UIViewController
    @objc func onComleted(message: String)
}
