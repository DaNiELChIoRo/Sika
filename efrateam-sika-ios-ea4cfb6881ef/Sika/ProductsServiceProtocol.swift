//
//  ProductsServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias ProductsResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol ProductsServiceProtocol {
    func execute(search: String, page: Int, completion: @escaping ProductsResponseClosure)
}
