//
//  NearbyStoresService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol NearbyStoresService {
    func getNearbyStores(lat: Double, lng: Double, page: Int, completion: @escaping (ServiceResponse) -> Void)
    func getStoresInDistrict(district: String, completion: @escaping (ServiceResponse) -> Void)
    func getStores(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping (ServiceResponse) -> Void)
}
