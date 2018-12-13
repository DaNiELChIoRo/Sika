//
//  NearbyStoresWebService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class NearbyStoresWebService: BaseService, NearbyStoresService {
    
    func getNearbyStores(lat: Double, lng: Double, page: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseurl = ServicesManager.shared.urlForEndpoint(endpoint: .Store)
        let url = baseurl + "?lat=\(lat)&lng=\(lng)&page=\(page)"
        super.callEndpoint(endPoint: url, headers: [:], completion: completion)
    }
    
    func getStoresInDistrict(district: String, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .StoresInDistrict)
        let url = String.init(format: baseUrl, district)
        super.callEndpoint(endPoint: url, headers: [:], completion: completion)
    }
    
    func getStores(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping (ServiceResponse) -> Void) {
        let baseUrl = ServicesManager.shared.urlForEndpoint(endpoint: .StoresByProduct)
        let url = String.init(format: baseUrl, productId) + "?lat=\(lat)&lng=\(lng)&metres=\(distance)"
        super.callEndpoint(endPoint: url, headers: [:], completion: completion)
    }
    
    override func parse(response: AnyObject, page: Int) -> [AnyObject]? {
        guard let response = response as? Dictionary<String, AnyObject>, let stores = response[AppConstants.Response.Response] as? [AnyObject] else {
            return nil
        }
        return stores
    }
    
}
