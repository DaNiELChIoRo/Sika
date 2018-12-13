//
//  StatesRepositoryImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class StatesRepositoryImpl: StatesRepository {

    let statesWebService = StatesWebService()
    let nearbyStoresWebService = NearbyStoresWebService()
    let districtsWebService = DistrictWebService()

    func getStates(countryId: Int, completion: @escaping StatesResponseClosure) {
        statesWebService.getStates(countryId: countryId) {
            response in
            var states: [State] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let state = State(JSONString: String(describing: json)) {
                                states.append(state)
                            }
                        }
                    } catch {
                        completion(false, states)
                    }
                }
                completion(true, states)
                break
            case .failure: completion(false, states)
                break
            case .notConnectedToInternet : completion(false, states)
                break
            }
        }
    }
    
    func getNearbyStores(lat: Double, lng: Double, page: Int, completion: @escaping NearbyStoresResponseClosure) {
        nearbyStoresWebService.getNearbyStores(lat: lat, lng: lng, page: page) {
            response in
            var stores: [ShopMarker] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let store = ShopMarker(JSONString: String(describing: json)) {
                                stores.append(store)
                            }
                        }
                    } catch {
                        completion(false, stores)
                    }
                }
                completion(true, stores)
                break
            case .failure: completion(false, stores)
            case .notConnectedToInternet : completion(false, stores)
            }
        }
    }

    func getDistricts(stateId: Int, completion: @escaping DistrictsResponseClosure) {
        districtsWebService.getDistricts(stateId: stateId) {
            response in
            switch response {
            case .success(let response) :
                if let districts = response as? [String] {
                    completion(true, districts)
                } else {
                    completion(false, [])
                }
            case .failure: completion(false, [])
            case .notConnectedToInternet : completion(false, [])
            }
        }
    }
    
    func getStores(in district: String, completion: @escaping NearbyStoresResponseClosure) {
        nearbyStoresWebService.getStoresInDistrict(district: district) {
            response in
            var stores: [ShopMarker] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let store = ShopMarker(JSONString: String(describing: json)) {
                                stores.append(store)
                            }
                        }
                    } catch {
                        completion(false, stores)
                    }
                }
                completion(true, stores)
            case .failure: completion(false, stores)
            case .notConnectedToInternet : completion(false, stores)
            }
        }
    }
    
    func getStores(by productId: Int, lat: Double, lng: Double, distance: Int, completion: @escaping NearbyStoresResponseClosure) {
        nearbyStoresWebService.getStores(by: productId, lat: lat, lng: lng, distance: distance) {
            response in
            var stores: [ShopMarker] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let store = ShopMarker(JSONString: String(describing: json)) {
                                stores.append(store)
                            }
                        }
                    } catch {
                        completion(false, stores)
                    }
                }
                completion(true, stores)
            case .failure: completion(false, stores)
            case .notConnectedToInternet : completion(false, stores)
            }
        }
    }

    
    func cancelAllRequest() {
        
    }
    
}
