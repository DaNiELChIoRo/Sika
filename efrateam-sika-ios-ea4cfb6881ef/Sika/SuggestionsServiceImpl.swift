//
//  SuggestionsServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class SuggestionsServiceImpl: UseCaseImpl, SuggestionsServiceProtocol {
    
    func execute(completion: @escaping SuggestionsResponseClosure) {
        (repository as! TaxonomyRepositoryImpl).getSuggestions(completion: completion)
    }
    
}
