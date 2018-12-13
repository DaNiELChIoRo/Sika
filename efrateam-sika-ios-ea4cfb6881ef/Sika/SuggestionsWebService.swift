//
//  SuggestionsWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class SuggestionsWebService: BaseService, SuggestionsService {

    func getSuggestions(completion: @escaping (ServiceResponse) -> Void) {
        let endpointUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Solution)
        super.callEndpoint(endPoint: endpointUrl, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let _response = response as? Dictionary<String, AnyObject>, let suggestions = _response[AppConstants.Response.Response] as? [AnyObject] {
            return suggestions
        }
        return nil
    }

}
