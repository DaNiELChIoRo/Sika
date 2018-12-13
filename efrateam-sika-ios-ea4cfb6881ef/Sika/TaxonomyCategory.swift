//
//  TaxonomyCategory.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class TaxonomyCategory: Mappable {

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
        createdAt <- map["created_at"]
        updatedAt <- map["updated_at"]
    }
    
}
