//
//  ProductInfoServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductInfoServiceImpl: UseCaseImpl, ProductInfoServiceProtocol {
    func execute(id: Int, completion: @escaping ProductInfoResponseClosure) {
        (repository as! ProductsRepository).getProductInfo(id: id, completion: completion)
    }
}
