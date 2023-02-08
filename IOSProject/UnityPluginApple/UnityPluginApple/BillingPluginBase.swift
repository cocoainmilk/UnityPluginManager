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

protocol BillingObserver : class
{
    func Update(transactions: [SKPaymentTransaction])
}

class BillingSubject : NSObject, SKPaymentTransactionObserver
{
    var observers : [BillingObserver] = []
    
    override init()
    {
        super.init()
        SKPaymentQueue.default().add(self)
    }
    
    deinit
    {
        SKPaymentQueue.default().remove(self)
    }
    
    func add(observer : BillingObserver)
    {
        observers.append(observer)
    }
    
    func remove(observer : BillingObserver)
    {
        observers.removeAll { (element) -> Bool in
            element === observer
        }
    }
    
    func paymentQueue(_ queue: SKPaymentQueue, updatedTransactions transactions: [SKPaymentTransaction])
    {
        observers.forEach { (observe) in
            observe.Update(transactions : transactions)
        }
    }
}

class BillingPluginBase : PluginBase, BillingObserver
{
    static var Subject = BillingSubject()
    
    public required init(pluginProtocol: PluginProtocol, id: Int, parameter: String?)
    {
        super.init(pluginProtocol: pluginProtocol, id: id, parameter: parameter)
        BillingPluginBase.Subject.add(observer: self)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    deinit {
        BillingPluginBase.Subject.remove(observer: self)
    }
    
    public func Update(transactions: [SKPaymentTransaction]) {
    }
    
    func getReceipt() -> String?
    {
        if let appStoreReceiptURL = Bundle.main.appStoreReceiptURL, FileManager.default.fileExists(atPath: appStoreReceiptURL.path)
        {
            do {
                let receiptData = try Data(contentsOf: appStoreReceiptURL, options: .alwaysMapped)
                return receiptData.base64EncodedString(options: [])
            }
            catch
            {
                return nil
            }
        }
        else
        {
            return nil
        }
    }
}
