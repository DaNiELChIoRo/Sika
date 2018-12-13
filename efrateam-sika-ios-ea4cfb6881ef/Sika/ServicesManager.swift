//
//  File.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/22/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

enum Environment: String {
    case Dev = "Dev"
    case Prod = "Prod"
}

struct PropertyListKey {
    static let URL = "URL"
    static let ExpiryTime = "ExpiryTime"
    static let RefreshTimers = "RefreshTimers" // Deals with an array of values
    static let PropertyList = "plist"
    
}

enum Endpoint: String {
    case Base = "Base"
    case Store = "Store"
    case Products = "Products"
    case State = "State"
    case Company = "Company"
    case Taxonomy = "Taxonomy"
    case Countries = "Countries"
    case ZipCode = "ZipCode"
    case StatesByCountry = "StatesByCountry"
    case Suggestions = "Suggestions"
    case Sliders = "Sliders"
    case Promotions = "Promotion"
    case Glossary = "Glossary"
    case StateDetails = "StateDetails"
    case StoresInDistrict = "StoresInDistrict"
    case StoresByProduct = "StoresByProduct"
    case Solution = "Solution"
    
    static let allValues = [Store, Products, State, Company, Taxonomy, Countries, ZipCode, StatesByCountry , Suggestions, Sliders, Promotions, Glossary, StateDetails, StoresInDistrict, StoresByProduct, Solution]
    
}

enum RefreshInterval: Int {
    case twentySeconds  =      20
    case oneMinute      =      60
    case twoMinutes     =     120
    case fiveMinutes    =     300
    case tenMinutes     =     600
    case halfHour       =    1800   // 30 minutes
    case oneHour        =    3600   // 60 minutes
    case oneDay         =   86400   // 24 hours
    case oneWeek        =  604800   //  7 days
    case oneMonth       = 2592000   // 30 days
}

class ServicesManager: NSObject {

    static let shared = ServicesManager()
    fileprivate var endpointsDictionary: Dictionary<String, AnyObject> = [:]

    let endpointKey = "Endpoints"
    let defaultRefreshInterval = RefreshInterval.tenMinutes

    override private init() {
        self.environment = .Prod
        
        super.init()
        
        if let rawEnv = UserDefaults.standard.object(forKey: AppConstants.Services.Environments) as? String,
            let env = Environment(rawValue: rawEnv){
            self.environment = env
        }
        
        self.updateEndpointsDictionary()
    }
    
    func updateEndpointsDictionary () {
        if let path = Bundle.main.path(forResource: endpointKey, ofType: PropertyListKey.PropertyList) {
            if let dictionary = NSDictionary(contentsOfFile: path) {
                self.endpointsDictionary = dictionary[environment.rawValue] as! Dictionary
            }
        }
    }
    
    var environment: Environment {
        didSet {
            self.updateEndpointsDictionary()
            UserDefaults.standard.set(environment.rawValue, forKey: AppConstants.Services.Environments)
        }
    }
    
    func urlForEndpoint(endpoint: Endpoint) -> String {
        return ((self.endpointsDictionary[Endpoint.Base.rawValue] as! Dictionary<String, AnyObject>)[PropertyListKey.URL] as! String).appending((self.endpointsDictionary[endpoint.rawValue] as! Dictionary<String, AnyObject>)[PropertyListKey.URL] as! String)
    }
    
    func refreshTimeForEndpoint(endpoint: Endpoint) -> Int {
        if let expirtyTimer: Int = (self.endpointsDictionary[endpoint.rawValue] as! Dictionary<String, AnyObject>)[PropertyListKey.ExpiryTime] as? Int{
            return expirtyTimer
        }
        
        print("No expiry timer found for endpoint: \(endpoint.rawValue)...setting default value")
        return defaultRefreshInterval.rawValue
    }

}
