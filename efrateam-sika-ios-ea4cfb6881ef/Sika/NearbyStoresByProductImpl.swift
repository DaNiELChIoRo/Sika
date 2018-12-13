//
//  NearbyStoresByProductImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/8/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class NearbyStoresByProductImpl: UseCaseImpl, NearbyStoresByProductServiceProtocol {
    
    func execute(by productId: Int, lat: Double, lng: Double, distance: Int,completion: @escaping NearbyStoresResponseClosure) {
        (repository as! StatesRepository).getStores(by: productId, lat: lat, lng: lng, distance: distance, completion: completion)
    }
    
}
