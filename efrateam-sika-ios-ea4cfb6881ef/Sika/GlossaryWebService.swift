//
//  GlossaryWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class GlossaryWebService: BaseService, GlossaryService {

    func getGlossary(completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Glossary)
        super.callEndpoint(endPoint: baseUrl, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let glossary = response[AppConstants.Response.Response] as? [AnyObject] {
            return glossary
        }
        return nil
    }

}
