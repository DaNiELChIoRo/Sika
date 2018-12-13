//
//  ProductsWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductsWebService: BaseService, ProductsService {
    
    func getProducts(id: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Products)
                        .appending("?taxonomy=\(id)")
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    
    func getProducts(search: String, page: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Products).appending("?search=\(search)")
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let products = response[AppConstants.Response.Response] as? [AnyObject] {
            return products
        }
        return nil
    }

}
