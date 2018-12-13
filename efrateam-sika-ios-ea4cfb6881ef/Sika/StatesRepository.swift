//
//  StatesRepository.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol StatesRepository: Repository {
    
    func getStates(countryId: Int, completion: @escaping StatesResponseClosure)
    func getNearbyStores(lat: Double, lng: Double, page: Int, completion: @escaping NearbyStoresResponseClosure)
    func getDistricts(stateId: Int, completion: @escaping DistrictsResponseClosure)
    func getStores(in district: String, completion: @escaping NearbyStoresResponseClosure)
    func getStores(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping NearbyStoresResponseClosure)
}
