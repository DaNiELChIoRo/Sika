//
//  TaxonomiesPresenter.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class TaxonomiesPresenter: BasePresenter {

    fileprivate let locator: TaxonomiesUseCaseLocator
    
    init(locator: TaxonomiesUseCaseLocator) {
        self.locator = locator
    }
    
    func getTaxonomies(completion: @escaping TaxonomyResponseClosure) {
        guard let useCase = self.locator.getUseCase(ofType: TaxonomyServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getProducts(search: String, page: Int, completion: @escaping ProductsResponseClosure) {
        guard let useCase = self.locator.getUseCase(ofType: ProductsServiceProtocol.self) else {
            return
        }
        useCase.execute(search: search, page: page, completion: completion)
    }
    
    func getProducts(id: Int, completion: @escaping ProductsResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: ProductsIdServiceProtocol.self) else {
            return
        }
        useCase.execute(id: id, completion: completion)
    }
    
    func getProductsByCategory(completion: @escaping ProductCategoryResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: ProductsCategoryServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getGlossary(completion: @escaping GlossaryResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: GlossaryServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getPromotions(completion: @escaping PromotionsResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: PromotionsServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getSuggestions(completion: @escaping SuggestionsResponseClosure) {
        guard let useCase = self.locator.getUseCase(ofType: SuggestionsServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getSliders(completion: @escaping SlidersResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: SlidersServiceProtocol.self) else {
            return
        }
        useCase.execute(completion: completion)
    }
    
    func getProductInfo(id: Int, completion: @escaping ProductInfoResponseClosure) {
        guard let useCase = locator.getUseCase(ofType: ProductInfoServiceProtocol.self) else {
            return
        }
        useCase.execute(id: id, completion: completion)
    }
    
}
