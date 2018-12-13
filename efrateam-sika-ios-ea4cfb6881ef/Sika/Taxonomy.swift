//
//  Taxonomy.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class Taxonomy: Mappable {

    var id: Int?
    var type: String?
    var value: String?
    var createdAt: String?
    var updatedAt: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        type <- map["type"]
        value <- map["value"]
        createdAt <- map["createdAt"]
        updatedAt <- map["updatedAt"]
    }
    
}
