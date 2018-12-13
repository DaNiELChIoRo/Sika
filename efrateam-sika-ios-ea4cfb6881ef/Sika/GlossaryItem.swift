//
//  GlossaryItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class GlossaryItem: Mappable {

    var id: Int?
    var title: String?
    var description: String?
    
    required init?(map: Map) {
        
    }

    func mapping(map: Map) {
        id <- map["id"]
        title <- map["title"]
        description <- map["description"]
    }

}
