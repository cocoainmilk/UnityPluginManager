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

class Plugin_billing_detail : BillingPluginBase, SKProductsRequestDelegate
{
    override open func process() throws
    {
        let ids : [String]! =  getParameter(key: "ProductId")
        let request  = SKProductsRequest(productIdentifiers: Set(ids))
        request.delegate = self
        request.start()
    }
    
    func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse)
    {
        let array  = response.products.map(
        {
            product in
            [
                "ProductId" : product.productIdentifier,
                "Price" : product.localizedPrice,
                "PriceValue" : Float(product.price.doubleValue),
                "CurrencyCode" : product.priceLocale.currencyCode
            ]
        })
        
        sendSuccessToUnity(parameter: ["Detail" : array])
    }
}

extension SKProduct {
    var localizedPrice: String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = priceLocale
        return formatter.string(from: price)!
    }
}
