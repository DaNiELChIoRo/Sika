//
//  ShopMarker.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class ShopMarker: Mappable {

    var id: Int?
    var name: String?
    var details: String?
    var street: String?
    var externalNumber: Int?
    var internalNumber: Int?
    var visualReference: String?
    var phone: String?
    var latitude: String?
    var longitude: String?
    var address: String?
    var createdAt: String?
    var updatedAt: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        details <- map["details"]
        street <- map["street"]
        externalNumber <- map["external_number"]
        internalNumber <- map["internal_number"]
        visualReference <- map["visual_reference"]
        phone <- map["phone"]
        
        latitude <- map["latitude"]
        longitude <- map["longitude"]
        
        address <- map["address"]
        createdAt <- map["created_at"]
        updatedAt <- map["updated_at"]
    }

}
