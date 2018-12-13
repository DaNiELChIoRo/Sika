//
//  FindStoresUseCaseLocator.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class FindStoreUseCaseLocator: UseCaseLocatorProtocol {
    
    static let defaultLocator = FindStoreUseCaseLocator(repository: StatesRepositoryImpl(),
                                                           service: StatesWebService())
    
    fileprivate let service: Service
    fileprivate let repository: Repository
    
    init(repository: Repository, service: Service) {
        self.repository = repository
        self.service = service
    }
    
    func getUseCase<T>(ofType type: T.Type) -> T? {
        switch String(describing: type) {
        case String(describing: StatesServiceProtocol.self):
            return buildUseCase(type: StatesServiceImpl.self)
        case String(describing: NearbyStoresServiceProtocol.self):
            return buildUseCase(type: NearbyStoresServiceImpl.self)
        case String(describing: DistrictsProtocol.self):
            return buildUseCase(type: DistrictsImpl.self)
        case String(describing: StoresInDistrictProtocol.self):
            return buildUseCase(type: StoresInDistrictImpl.self)
        case String(describing: NearbyStoresByProductServiceProtocol.self):
            return buildUseCase(type: NearbyStoresByProductImpl.self)
        default:
            return nil
        }
    }
}

private extension FindStoreUseCaseLocator {
    func buildUseCase<U: UseCaseImpl, R>(type: U.Type) -> R? {
        return U(repository: repository, service: service) as? R
    }
}
