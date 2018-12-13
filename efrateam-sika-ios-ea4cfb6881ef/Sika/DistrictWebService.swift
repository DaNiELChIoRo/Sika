//
//  DistrictWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/12/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class DistrictWebService: BaseService, DistrictService {
    
    struct Keys {
        static let districts = "districts"
    }

    func getDistricts(stateId: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .StateDetails)
        let endpoint = String.init(format: baseUrl, stateId)
        super.callEndpoint(endPoint: endpoint, headers: [:], completion: completion)
    }

    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        guard let payload = response as? Dictionary<String, AnyObject>, let response = payload[AppConstants.Response.Response] as? Dictionary<String, AnyObject>, let districts = response[Keys.districts] as? [AnyObject] else {
            return nil
        }
        return districts
    }
    
}
