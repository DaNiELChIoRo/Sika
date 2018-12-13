//
//  ProblemStep.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class ProblemStep: Mappable {

    var image: String?
    var caption: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        image <- map["image"]
        caption <- map["caption"]
    }


}
