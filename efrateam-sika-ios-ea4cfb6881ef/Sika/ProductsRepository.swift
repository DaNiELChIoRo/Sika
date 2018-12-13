//
//  ProductsRepository.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol ProductsRepository : Repository {
    func getProducts(search: String, page: Int, completion: @escaping ProductsResponseClosure)
    func getProductInfo(id: Int, completion: @escaping ProductInfoResponseClosure)
    func getProductByCategory(completion: @escaping ProductCategoryResponseClosure)
    func getPromotions(completion: @escaping PromotionsResponseClosure)
}
