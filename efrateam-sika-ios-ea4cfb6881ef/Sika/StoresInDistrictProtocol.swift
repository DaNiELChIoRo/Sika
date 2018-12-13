//
//  StoresInDistrictProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol StoresInDistrictProtocol {
    func execute(in district: String, completion: @escaping NearbyStoresResponseClosure)
}
