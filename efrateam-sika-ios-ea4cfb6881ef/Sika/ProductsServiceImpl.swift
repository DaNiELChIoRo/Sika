//
//  ProductsServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductsServiceImpl: UseCaseImpl, ProductsServiceProtocol {
    
    func execute(search: String, page: Int, completion: @escaping ProductsResponseClosure) {
        (repository as! ProductsRepositoryImpl).getProducts(search: search, page: page, completion: completion)
    }
    
}
