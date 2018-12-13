
//
//  TaxonomyServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias TaxonomyResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol TaxonomyServiceProtocol {
    func execute(completion: @escaping TaxonomyResponseClosure)
}
