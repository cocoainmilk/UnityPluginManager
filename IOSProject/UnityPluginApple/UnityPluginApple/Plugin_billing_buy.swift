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

class Plugin_billing_buy: BillingPluginBase, SKProductsRequestDelegate
{
    override open func process() throws
    {
        print("apple billing buy")
        
        let productId : String! =  getParameter(key: "ProductId")
        let request  = SKProductsRequest(productIdentifiers: Set([productId]))
        request.delegate = self
        request.start()
    }
        
    func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse)
    {
        if !response.products.isEmpty
        {
            let payment = SKMutablePayment(product: response.products[0])
            payment.quantity = 1
            SKPaymentQueue.default().add(payment)
        }
        else
        {
            sendFailToUnity()
        }
    }
    
    override func Update(transactions: [SKPaymentTransaction])
    {
        for transaction in transactions
        {
            if transaction.transactionState == .purchased
            {
                sendResult(transactionId: transaction.transactionIdentifier!)
                return
            }
            else if transaction.transactionState == .deferred
            {
                sendPendingUntilAllowToUnity()
                return
            }
            else if transaction.transactionState == .failed
            {
                SKPaymentQueue.default().finishTransaction(transaction)
                if (transaction.error as? SKError)?.code == .paymentCancelled
                {
                    sendCancelToUnity()
                }
                else
                {
                    sendFailToUnity()
                }
                return
            }
        }
    }
    
    func sendResult(transactionId : String)
    {
        sendSuccessToUnity(parameter: [
            "Receipt" : getReceipt(),
            "PurchaseToken" :transactionId
        ])
    }
}

