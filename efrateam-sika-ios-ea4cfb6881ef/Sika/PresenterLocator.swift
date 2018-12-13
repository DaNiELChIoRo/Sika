//
//  PresenterLocator.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation


enum PresenterType : String {
    case homeViewController = "homeVC"
    case searchViewController = "searchVC"
    case findStoreViewController = "findStoreVC"
}

protocol PresenterLocatorProtocol {
    static func getPresenter(ofType type: PresenterType) -> AnyObject?
}

class PresenterLocator: PresenterLocatorProtocol {

    static func getPresenter(ofType type: PresenterType) -> AnyObject? {
        switch type {
           
        case .homeViewController:
            return TaxonomiesPresenter(locator: TaxonomiesUseCaseLocator.defaultLocator)
        case .searchViewController:
            return TaxonomiesPresenter(locator: TaxonomiesUseCaseLocator(repository: ProductsRepositoryImpl(), service: ProductsWebService()))
        case .findStoreViewController:
            return FindStoresPresenter(locator: FindStoreUseCaseLocator.defaultLocator)
        }
    }
}
