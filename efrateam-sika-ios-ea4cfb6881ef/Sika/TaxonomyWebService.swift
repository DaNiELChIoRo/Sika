//
//  TaxonomyWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class TaxonomyWebService: BaseService, TaxonomyService {

    func getTaxonomies(completion: @escaping (ServiceResponse) -> Void) {
        let baseurl = ServicesManager.shared.urlForEndpoint(endpoint: .Taxonomy)
        super.callEndpoint(endPoint: baseurl, headers: [:], completion: completion)
    }

    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let _response = response as? Dictionary<String, AnyObject>, let taxonomy = _response[AppConstants.Response.Response] as? [AnyObject] {
            return taxonomy
        }
        return nil
    }

}
