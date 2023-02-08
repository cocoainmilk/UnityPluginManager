//
//  LoginPluginViewController.swift
//  UnityPluginNaver
//
//  Created by  on 2020/01/05.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UnityPluginCore
import StoreKit

class Plugin_billing_check_open : BillingPluginBase
{
    override open func process() throws
    {
        print("apple billing check open")
        
        if !SKPaymentQueue.canMakePayments()
        {
            sendNoPermissionToUnity()
            return
        }
        
        let transactions = SKPaymentQueue.default().transactions
        if !transactions.isEmpty
        {
            if transactions[0].transactionState == .deferred
            {
                sendPendingUntilAllowToUnity()
            }
            else
            {
                sendPendingToUnity()
            }
            return
        }
        
        sendSuccessToUnity(parameter: nil)
    }
}

