//
//  StateDetailsWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class StateDetailsWebService: BaseService, StateDetailsService {
 
    /*func getStateDetails(state: String, completion: @escaping (ServiceResponse) -> Void) {
        let endpoint = ServicesManager.shared.urlForEndpoint(endpoint: .StoreDetails)
        let url = String.init(format: endpoint, state)
        super.callEndpoint(endPoint: url, headers: [:], completion: completion)
    }*/
    
    func getStateDetails(stateId: Int, completion: @escaping (ServiceResponse) -> Void) {
        
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        return nil
    }
    
}
