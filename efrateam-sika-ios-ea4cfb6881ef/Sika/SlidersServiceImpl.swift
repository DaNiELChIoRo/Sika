//
//  SlidersServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class SlidersServiceImpl: UseCaseImpl, SlidersServiceProtocol {
    func execute(completion: @escaping SlidersResponseClosure) {
        (repository as! TaxonomyRepository).getSliders(completion: completion)
    }
}
