//
//  ProductsRepositoryImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class ProductsRepositoryImpl : ProductsRepository {
    
    let productsWebService = ProductsWebService()
    let productInfoWebService = ProductInfoWebService()
    let productCategoryWebService = ProductCategoryWebService()
    let promotionsWebService = PromotionsWebService()
    
    func getProducts(search: String, page: Int, completion: @escaping ProductsResponseClosure) {
        productsWebService.getProducts(search: search, page: page) {
            response in
            var products: [Product] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let state = Product(JSONString: String(describing: json)) {
                                products.append(state)
                            }
                        }
                    } catch {
                        completion(false, products)
                    }
                }
                completion(true, products)
                break
            case .failure: completion(false, products)
                break
            case .notConnectedToInternet : completion(false, products)
                break
            }
            
        }
    }
    
    func getProductsWithId(id: Int, completion: @escaping ProductsResponseClosure) {
        productsWebService.getProducts(id: id) {
            response in
            var products: [Product] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let state = Product(JSONString: String(describing: json)) {
                                products.append(state)
                            }
                        }
                    } catch {
                        completion(false, products)
                    }
                }
                completion(true, products)
                break
            case .failure: completion(false, products)
                break
            case .notConnectedToInternet : completion(false, products)
                break
            }
            
        }
    }
    
    func getProductInfo(id: Int, completion: @escaping ProductInfoResponseClosure) {
        productInfoWebService.getProductInfo(id: id, completion: {
            response in

            switch response {
            case .success(let response):
                guard let dic = response.first as? Dictionary<String, AnyObject> else {
                    completion(false, [])
                    break
                }
                do {
                    let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                    if let json = String(data: data,encoding: String.Encoding.utf8), let product = Product(JSONString: String(describing: json)) {
                        completion(true, [product])
                    } else {
                        completion(false, [])
                    }
                } catch {
                    completion(false, [])
                }
            case .failure: completion(false, [])
            case .notConnectedToInternet : completion(false, [])
            }
        })
    }
    
    func getProductByCategory(completion: @escaping ProductCategoryResponseClosure) {
        productCategoryWebService.getProductsByCategory(completion: {
            response in
            switch response {
            case .success(let response):
                var categories: [TaxonomyCategory] = []
                for element in response {
                    if let dic = element as? Dictionary<String, AnyObject> {
                        do {
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let category = TaxonomyCategory(JSONString: String(describing: json)) {
                                categories.append(category)
                            }
                        } catch {
                            completion(false, [])
                        }
                    }
                }
                completion(!categories.isEmpty, categories)
            case .failure: completion(false, [])
            case .notConnectedToInternet : completion(false, [])
            }
        })
    }
    
    func getPromotions(completion: @escaping PromotionsResponseClosure) {
        promotionsWebService.getPromotions(completion: {
            response in
            switch response {
            case .success(let response):
                var promotions: [Promotion] = []
                for element in response {
                    if let dic = element as? Dictionary<String, AnyObject> {
                        do {
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let promotion = Promotion(JSONString: String(describing: json)) {
                                promotions.append(promotion)
                            }
                        } catch {
                            completion(false, [])
                        }
                    }
                }
                completion(!promotions.isEmpty, promotions)
            case .failure: completion(false, [])
            case .notConnectedToInternet : completion(false, [])

            }
        })
    }

    func cancelAllRequest() {
        
    }

}
