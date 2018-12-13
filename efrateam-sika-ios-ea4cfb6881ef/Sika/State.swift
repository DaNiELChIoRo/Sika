//
//  State.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class State: Mappable {
    
    var id: Int?
    var name: String?
    var createdAt: String?
    var updatedAt: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        createdAt <- map["created_at"]
        updatedAt <- map["updated_at"]
    }

}
