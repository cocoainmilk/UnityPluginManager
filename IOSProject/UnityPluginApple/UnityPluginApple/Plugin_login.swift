//
//  LoginPluginViewController.swift
//  UnityPluginNaver
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UnityPluginCore
import AuthenticationServices

class Plugin_login : PluginViewControllerBase, ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding
{
    override open func process() throws
    {
        print("apple login")
        
        let userId : String? = getParameter(key: "AppleUserId")
        if userId != nil && !userId!.isEmpty
        {
            ASAuthorizationAppleIDProvider().getCredentialState(forUserID: userId!) { (credentialState, error) in
                switch credentialState
                {
                case .authorized:
                    DispatchQueue.main.async {
                        self.sendResult(userId: userId!, token: nil)
                    }
                    break;
                default:
                    DispatchQueue.main.async {
                        self.request()
                    }
                    break;
                }
            }
        }
        else
        {
            request()
        }

    }
    
    func request()
    {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.email]
        
        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }
    
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.view.window!
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization)
    {
        let appleIDCredential = authorization.credential as! ASAuthorizationAppleIDCredential
        let userId = appleIDCredential.user
        let authCode = String(data: appleIDCredential.authorizationCode!, encoding: .utf8)
        sendResult(userId: userId, token: authCode!)
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error)
    {
        let code = (error as? ASAuthorizationError)?.code;
        switch(code)
        {
        case .canceled:
            sendCancelToUnity()
            break;
        default:
            print("apple login : ", code!)
            sendFailToUnity()
        }
    }
    
    func sendResult(userId : String, token: String?)
    {
        var parameter: [String : Any] = [:]
        parameter["AppleUserId"] = userId
        parameter["AccessToken"] = token
        sendSuccessToUnity(parameter: parameter)
    }
}
