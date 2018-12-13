//
//  ProductInfoWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductInfoWebService: BaseService, ProductInfoService {

    func getProductInfo(id: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Products).appending("/\(id)")
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let product = response[AppConstants.Response.Response] {
            return [product]
        }
        return nil
    }
    
}
