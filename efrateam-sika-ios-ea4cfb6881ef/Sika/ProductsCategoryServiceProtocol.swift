//
//  ProductsCategoryServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias ProductCategoryResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol ProductsCategoryServiceProtocol {
    func execute(completion: @escaping ProductCategoryResponseClosure)
}
