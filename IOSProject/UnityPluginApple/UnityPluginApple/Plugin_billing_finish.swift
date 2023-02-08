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

class Plugin_billing_finish : BillingPluginBase
{
    override open func process() throws
    {
        print("apple billing finish")

        for transaction in SKPaymentQueue.default().transactions
        {
            if transaction.transactionState == .purchased
            {
                SKPaymentQueue.default().finishTransaction(transaction)
                sendSuccessToUnity(parameter: nil)
                return
            }
        }
        
        sendFailToUnity()
    }
}

