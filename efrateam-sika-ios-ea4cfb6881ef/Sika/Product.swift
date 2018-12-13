//
//  Product.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class Product: Mappable {
   
    var id: Int?
    var fileId: Int?
    var name: String?
    var details: String?
    var createdAt: String?
    var updatedAt: String?
    var photo: ProductPhoto?
    var related: [Product]?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        fileId <- map["file_id"]
        name <- map["name"]
        details <- map["details"]
        createdAt <- map["createdAt"]
        updatedAt <- map["updatedAt"]
        photo <- map["photo"]
        related <- map["related"]
    }
    
}
