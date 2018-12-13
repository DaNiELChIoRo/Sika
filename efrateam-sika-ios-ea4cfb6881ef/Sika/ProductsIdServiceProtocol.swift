//
//  ProductsIdServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol ProductsIdServiceProtocol {
    func execute(id: Int, completion: @escaping ProductsResponseClosure)
}
