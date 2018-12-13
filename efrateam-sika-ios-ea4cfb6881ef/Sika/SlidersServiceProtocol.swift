//
//  SlidersServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias SlidersResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol SlidersServiceProtocol {
    func execute(completion: @escaping SlidersResponseClosure)
}
