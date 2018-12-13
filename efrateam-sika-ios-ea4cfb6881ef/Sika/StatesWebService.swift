//
//  StatesWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class StatesWebService: BaseService, StatesService {

    func getStates(countryId: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseurl = ServicesManager.shared.urlForEndpoint(endpoint: .StatesByCountry)
        let url = baseurl.replacingOccurrences(of: "{id}", with: String(countryId))
        super.callEndpoint(endPoint: url, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let _response = response as? Dictionary<String, AnyObject>, let states = _response[AppConstants.Response.Response] as? [AnyObject] {
            return states
        }
        return nil
    }

}
