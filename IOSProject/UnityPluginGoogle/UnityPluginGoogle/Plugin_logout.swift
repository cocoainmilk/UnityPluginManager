//
//  Plugin_logout.swift
//  UnityPluginGoogle
//
//  Created by  on 2020/10/11.
//

import Foundation
import UnityPluginCore
import GoogleSignIn

class Plugin_logout : PluginBase
{
    override func process() throws {
        GIDSignIn.sharedInstance().signOut()
        sendSuccessToUnity(parameter: nil)
    }
}
