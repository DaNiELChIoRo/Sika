//
//  ImageOperations.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

enum ImageRecordState {
    case New, Downloaded, Failed
}

class ImageRecord {
    
    let name: String
    let url: URL?
    var state = ImageRecordState.New
    var image = UIImage(named: "Placeholder")
        
    init(name:String, url: URL?) {
        self.name = name
        self.url = url
    }
}

