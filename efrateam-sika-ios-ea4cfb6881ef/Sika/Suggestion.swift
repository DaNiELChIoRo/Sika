//
//  Suggestion.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class Suggestion: Mappable {
    
    var title: String?
    //var color: String?
    var image: String?
    var items: [ProblemItem]?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        title <- map["title"]
        image <- map["image"]
        //color <- map["color"]
        items <- map["items"]
    }
    
}
