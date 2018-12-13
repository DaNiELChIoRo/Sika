//
//  ProductCategoryWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductCategoryWebService: BaseService, ProductCategoryService {

    func getProductsByCategory(completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Taxonomy).appending("?type=category")
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let taxonomies = response[AppConstants.Response.Response] as? [AnyObject] {
            return taxonomies
        }
        return nil
    }

}
