//
//  PromotionsServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias PromotionsResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol PromotionsServiceProtocol {
    func execute(completion: @escaping PromotionsResponseClosure)
}
