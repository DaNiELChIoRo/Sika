//
//  ProblemItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class ProblemItem : Mappable {
    
    var title: String?
    var image: String?
    var caption: String?
    var products: [SuggestionProduct]?
    var steps: [ProblemStep]?
    var cause: [String]?
    var glossary: String?
    var demo: String?

    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        title <- map["title"]
        image <- map["image"]
        caption <- map["caption"]
        products <- map["products"]
        steps <- map["steps"]
        cause <- map["cause"]
        glossary <- map["glossary"]
        demo <- map["demo"]
    }

    
}
