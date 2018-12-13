//
//  FindStoresPresenter.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class FindStoresPresenter: BasePresenter {

    fileprivate let locator: FindStoreUseCaseLocator
    
    init(locator: FindStoreUseCaseLocator) {
        self.locator = locator
        
    }
    
    func getStates(countryId: Int, completion: @escaping StatesResponseClosure) {
        guard let useCase = self.locator.getUseCase(ofType: StatesServiceProtocol.self) else {
            return
        }
        useCase.execute(countryId: countryId, completion: completion)
    }

    func getNearbyStores(lat: Double, lng: Double, page: Int, completion: @escaping NearbyStoresResponseClosure) {
        guard let useCase = self.locator.getUseCase(ofType: NearbyStoresServiceProtocol.self) else {
            return
        }
        useCase.execute(lat: lat, lng: lng, page: page, completion: completion)
    }
    
    func getDistricts(stateId: Int, completion: @escaping DistrictsResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: DistrictsProtocol.self) else {
            return
        }
        useCase.execute(stateId: stateId, completion: completion)
    }
    
    func getStores(in district: String, completion: @escaping NearbyStoresResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: StoresInDistrictProtocol.self) else {
            return
        }
        useCase.execute(in: district, completion: completion)
    }
    
    func getStores(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping NearbyStoresResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: NearbyStoresByProductServiceProtocol.self) else {
            return
        }
        useCase.execute(by: productId, lat: lat, lng: lng, distance: distance, completion: completion)
    }
    
}
