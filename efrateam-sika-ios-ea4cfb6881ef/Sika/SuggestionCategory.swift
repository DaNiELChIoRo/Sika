//
//  SuggestionCategory.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class SuggestionCategory: Mappable {
    
    var title: String?
    var items: [Suggestion]?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        title <- map["title"]
        items <- map["items"]
    }
    
}
