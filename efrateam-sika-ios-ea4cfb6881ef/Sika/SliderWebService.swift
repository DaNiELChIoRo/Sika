//
//  SliderWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class SliderWebService: BaseService, SliderService {

    func getSliders(completion: @escaping (ServiceResponse) -> Void) {
        let endpointUrl = ServicesManager.shared.urlForEndpoint(endpoint: .Sliders)
        super.callEndpoint(endPoint: endpointUrl, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        if let response = response as? Dictionary<String, AnyObject>, let sliders = response[AppConstants.Response.Response] as? [AnyObject] {
            return sliders
        }
        return nil
    }

}
