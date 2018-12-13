//
//  DistrictsImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class DistrictsImpl: UseCaseImpl, DistrictsProtocol {
    
    func execute(stateId: Int, completion: @escaping DistrictsResponseClosure) {
        (repository as! StatesRepository).getDistricts(stateId: stateId, completion: completion)
    }
    
}
