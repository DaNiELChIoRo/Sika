//
//  TaxonomyRepository.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol TaxonomyRepository: Repository {
    func getTaxonomies(completion: @escaping TaxonomyResponseClosure)
    func getSuggestions(completion: @escaping SuggestionsResponseClosure)
    func getSliders(completion: @escaping SlidersResponseClosure)
    func getGlossary(completion: @escaping GlossaryResponseClosure)
}
