//
//  Billing_check_ackPluginViewController.swift
//  UnityPluginApple
//
//  Created by  on 2020/08/02.
//  Copyright © 2020 Company. All rights reserved.
//

import Foundation
import UnityPluginCore
import StoreKit

class Plugin_billing_check_ack : BillingPluginBase
{
    override open func process() throws
    {
        print("apple billing check ack")
    
        for transaction in SKPaymentQueue.default().transactions
        {
            if transaction.transactionState == .purchased
            {
                sendResult(transactionId: transaction.transactionIdentifier!, productId: transaction.payment.productIdentifier)
                return
            }
            else if transaction.transactionState == .failed
            {
                SKPaymentQueue.default().finishTransaction(transaction)
            }
        }
        
        sendSuccessToUnity(parameter: nil)
    }
    
    func sendResult(transactionId : String, productId : String)
    {
        sendSuccessToUnity(parameter: [
            "Receipt" : getReceipt(),
            "PurchaseToken" : transactionId,
            "ProductId" : productId
        ])
    }
}

