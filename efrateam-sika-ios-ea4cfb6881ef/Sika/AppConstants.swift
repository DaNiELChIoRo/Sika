//
//  AppConstants.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 26/05/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
struct AppConstants {
    
    struct Mail {
        static let destination = "seguimiento.hunt@gmail.com"
    }
    
    struct States {
        static let States =  "states"
    }
    
    struct Countries {
        static let Default: Int = 148
    }

    struct Services {
        // Seconds
        static let Timeout = 30.0
        static let ApiKeyHeaderId = "AppKey"
        static let Environments = "Environments"
    }
    
    struct Response {
        static let Code = "code"
        static let Response = "response"
    }
    
    struct Icons {
        
        static let SikaHeader = "SikaHeader"
        static let Logo = "SikaLogo"
        static let Menu = "Menu"
        static let Search = "Search"
        static let Marker = "Marker"
        static let Back = "Back"
        static let Splash = "SikaSplash"
        static let HomeIconWhite = "HomeIconWhite"
        
        struct MenuIcons {
            static let MenuHome = "MenuHome"
            static let MenuOffer = "MenuOffer"
            static let MenuEmail = "MenuEmail"
            static let MenuLocation = "MenuLocation"
            static let MenuSearch = "MenuSearch"
            static let MenuGlossary = "MenuGlossary"
        }
        
    }
    
    struct Cells {
        static let State = "StateTableViewCell"
        static let SearchProduct = "SearchProductTableViewCell"
        static let SearchFilteredProduct = "SearchFilteredProductTableViewCell"
        static let Glossary = "GlossaryCell"
        static let NearStore = "NearStoreTableViewCell"
    }
    
    struct ViewControllers {
        static let Map = "MapViewController"
        static let States = "StatesViewController"
        static let Side = "SideViewController"
        static let Home = "HomeViewController"
        static let Stores = "FindStoreViewController"
        static let Search = "SearchViewController"
        static let Product = "ProductViewController"
        static let Promotions = "PromotionsViewController"
        static let Glossary = "GlossaryViewController"
        static let StoreDetails = "StoreDetailViewController"
        static let NearStore = "NearStoreViewController"
    }
    
    
}
