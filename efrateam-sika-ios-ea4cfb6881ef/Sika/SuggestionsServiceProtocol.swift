//
//  SuggestionsServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias SuggestionsResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol SuggestionsServiceProtocol {
    func execute(completion: @escaping SuggestionsResponseClosure)
}
