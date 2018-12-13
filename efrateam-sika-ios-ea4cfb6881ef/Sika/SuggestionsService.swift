//
//  SuggestionsService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol SuggestionsService {
    func getSuggestions(completion: @escaping (ServiceResponse) -> Void)
}
