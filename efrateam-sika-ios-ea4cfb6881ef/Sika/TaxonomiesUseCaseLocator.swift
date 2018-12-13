//
//  TaxonomiesUseCaseLocator.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class TaxonomiesUseCaseLocator: UseCaseLocatorProtocol {

    static let defaultLocator = TaxonomiesUseCaseLocator(repository: TaxonomyRepositoryImpl(), service: TaxonomyWebService())
    
    fileprivate let service: Service
    fileprivate let repository: Repository
    
    init(repository: Repository, service: Service) {
        self.repository = repository
        self.service = service
    }
    
    func getUseCase<T>(ofType type: T.Type) -> T? {
        switch String(describing: type) {
        case String(describing: TaxonomyServiceProtocol.self):
            return buildUseCase(type: TaxonomyServiceImpl.self)
        case String(describing: ProductsServiceProtocol.self):
            return buildUseCase(type: ProductsServiceImpl.self)
        case String(describing: SuggestionsServiceProtocol.self):
            return buildUseCase(type: SuggestionsServiceImpl.self)
        case String(describing: ProductsIdServiceProtocol.self):
            return buildUseCase(type: ProductsIdServiceImpl.self)
        case String(describing: SlidersServiceProtocol.self):
            return buildUseCase(type: SlidersServiceImpl.self)
        case String(describing: ProductInfoServiceProtocol.self):
            return buildUseCase(type: ProductInfoServiceImpl.self)
        case String(describing: ProductsCategoryServiceProtocol.self):
            return buildUseCase(type: ProductsCategoryServiceImpl.self)
        case String(describing: PromotionsServiceProtocol.self):
            return buildUseCase(type: PromotionsServiceImpl.self)
        case String(describing: GlossaryServiceProtocol.self):
            return buildUseCase(type: GlossaryServiceImpl.self)
        default:
            return nil
        }
    }
}

private extension TaxonomiesUseCaseLocator {
    func buildUseCase<U: UseCaseImpl, R>(type: U.Type) -> R? {
        return U(repository: repository, service: service) as? R
    }
}
