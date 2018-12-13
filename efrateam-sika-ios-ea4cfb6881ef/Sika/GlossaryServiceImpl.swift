//
//  GlossaryServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class GlossaryServiceImpl: UseCaseImpl, GlossaryServiceProtocol {

    func execute(completion: @escaping GlossaryResponseClosure) {
        (repository as! TaxonomyRepository).getGlossary(completion: completion)
    }

}
