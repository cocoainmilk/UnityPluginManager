//
//  Plugin_login.swift
//  UnityPluginGoogle
//
//  Created by  on 2020/10/11.
//

import Foundation
import UnityPluginCore
import GoogleSignIn

class Plugin_login : PluginViewControllerBase, GIDSignInDelegate
{
    override func process() throws {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onOpenURL),
            name: Plugin.NotificationOpenURL,
            object: nil)
        
        
        let clientId = Bundle.main.object(forInfoDictionaryKey: "GoogleClientID") as! String
        let manager = GIDSignIn.sharedInstance()!
        manager.clientID = clientId
        manager.delegate = self
        manager.presentingViewController = self
        if manager.hasPreviousSignIn()
        {
            DispatchQueue.main.async {
                manager.restorePreviousSignIn()
            }
        }
        else
        {
            DispatchQueue.main.async {
                manager.signIn()
            }
        }
    }
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if let error = error
        {
            if(error as NSError).code == GIDSignInErrorCode.hasNoAuthInKeychain.rawValue
            {
                //GIDSignIn.sharedInstance()?.signIn()
            }
            else if (error as NSError).code == GIDSignInErrorCode.canceled.rawValue
            {
                sendCancelToUnity()
            }
            else if (error as NSError).code == GIDSignInErrorCode.EMM.rawValue
            {
                sendCancelToUnity()
            }
            else
            {
                sendFailToUnity()
            }
            return
        }
        
        sendResult(token: user.authentication.idToken)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func onOpenURL(notification: NSNotification)
    {
        let url = notification.userInfo!["url"] as! URL
        GIDSignIn.sharedInstance().handle(url)
    }
    
    func sendResult(token: String?)
    {
        var parameter: [String : Any] = [:]
        parameter["AccessToken"] = token
        sendSuccessToUnity(parameter: parameter)
    }
}
