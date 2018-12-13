//
//  NearbyStoresServiceProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias NearbyStoresResponseClosure = (_ succes: Bool, _ response: [AnyObject]) -> Void

protocol NearbyStoresServiceProtocol {
    func execute(lat: Double, lng: Double, page: Int, completion: @escaping NearbyStoresResponseClosure)
}
