//
//  NearbyStoresByProductServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/8/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol NearbyStoresByProductServiceProtocol {
    func execute(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping NearbyStoresResponseClosure)
}
