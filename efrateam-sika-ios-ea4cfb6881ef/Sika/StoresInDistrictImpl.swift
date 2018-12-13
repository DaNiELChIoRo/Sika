//
//  StoresInDistrictImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class StoresInDistrictImpl: UseCaseImpl, StoresInDistrictProtocol {
    
    func execute(in district: String, completion: @escaping NearbyStoresResponseClosure) {
        (repository as! StatesRepository).getStores(in: district, completion: completion)
    }

}
