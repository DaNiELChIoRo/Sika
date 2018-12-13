//
//  SuggestionProduct.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class SuggestionProduct: Mappable {

    var id: Int?
    var title: String?
    var image: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        title <- map["title"]
        image <- map["image"]
    }
    
}
