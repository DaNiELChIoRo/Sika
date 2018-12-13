//
//  NearbyStoresServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class NearbyStoresServiceImpl: UseCaseImpl, NearbyStoresServiceProtocol {
    
    func execute(lat: Double, lng: Double, page: Int, completion: @escaping NearbyStoresResponseClosure) {
        (repository as! StatesRepositoryImpl).getNearbyStores(lat: lat, lng: lng, page: page, completion: completion)
    }
    
}
