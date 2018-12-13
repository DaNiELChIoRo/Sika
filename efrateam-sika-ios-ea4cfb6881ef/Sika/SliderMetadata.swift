//
//  SliderMetadata.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class SliderMetadata: Mappable {

    var id: Int?
    var name: String?
    var link: String?
    var description: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        link <- map["link"]
        description <- map["description"]
    }

}
