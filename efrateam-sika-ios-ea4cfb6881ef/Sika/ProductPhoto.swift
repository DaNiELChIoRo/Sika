//
//  ProductPhoto.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import ObjectMapper

class ProductPhoto: Mappable {

    var id: Int?
    var name: String?
    var url: String?
    var thumbnails: [ProductPhotoThumbnail]?
    
    required init?(map: Map) {
        
    }

    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        url <- map["url"]
        thumbnails <- map["thumbnails"]
    }
    
    func getLowestThumbnailResolution() -> ProductPhotoThumbnail? {
        guard let thumbnails = self.thumbnails else {
            return nil
        }
        return thumbnails.max { $0.width! < $1.width! }
    }
    
}

class ProductPhotoThumbnail: Mappable {

    var id: Int?
    var name: String?
    var width: Int?
    var height: Int?
    var url: String?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        width <- map["width"]
        height <- map["height"]
        url <- map["url"]
    }
    
}
