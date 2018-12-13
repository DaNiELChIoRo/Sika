//
//  StatesServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class StatesServiceImpl: UseCaseImpl, StatesServiceProtocol {

    func execute(countryId: Int, completion: @escaping StatesResponseClosure) {
        (repository as! StatesRepositoryImpl).getStates(countryId: countryId, completion: completion)
    }
    
}
