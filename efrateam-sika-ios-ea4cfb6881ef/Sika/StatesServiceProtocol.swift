//
//  StatesServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias StatesResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol StatesServiceProtocol {
    func execute(countryId: Int, completion: @escaping StatesResponseClosure)
}
