//
//  SliderItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class SliderItem: Mappable {

    var image: String?
    var type: String?
    var metadata: SliderMetadata?

    required init?(map: Map) {
        
    }

    func mapping(map: Map) {
        image  <- map["image"]
        type <- map["type"]
        metadata <- map["metadata"]
    }

}
