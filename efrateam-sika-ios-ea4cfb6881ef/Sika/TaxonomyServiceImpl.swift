//
//  TaxonomyServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class TaxonomyServiceImpl: UseCaseImpl, TaxonomyServiceProtocol {
    
    func execute(completion: @escaping TaxonomyResponseClosure) {
        (repository as! TaxonomyRepositoryImpl).getTaxonomies(completion: completion)
    }
    
}
