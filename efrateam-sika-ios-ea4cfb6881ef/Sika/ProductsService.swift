//
//  ProductsService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol ProductsService : Service {
    
    func getProducts(search: String, page: Int, completion: @escaping (ServiceResponse) -> Void)
    func getProducts(id: Int, completion: @escaping (ServiceResponse) -> Void)
    
}
