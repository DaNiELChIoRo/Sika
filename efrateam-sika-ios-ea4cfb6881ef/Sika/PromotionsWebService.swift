//
//  PromotionsWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class PromotionsWebService: BaseService, PromotionsService {
    
    func getPromotions(completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Promotions)
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let promotions = response[AppConstants.Response.Response] as? [AnyObject] {
            return promotions
        }
        return nil
    }
    
}
