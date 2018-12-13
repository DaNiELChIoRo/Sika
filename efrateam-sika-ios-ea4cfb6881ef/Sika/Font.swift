//
//  Font.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

struct Font {
    
    struct FontName {
        static let OpenSansFamily = "OpenSans"
        static let OpenSansLight = "OpenSans-Light"
        static let OpenSansLightItalic = "OpenSansLight-Italic"
        static let OpenSansRegular = "OpenSans"
        static let OpenSansSemibold = "OpenSans-Semibold"
        static let OpenSansBold = "OpenSans-Bold"
    }
    
    struct FontColors {
        static let TaxonomyColors: [UIColor] = [
            UIColor.init(white: 0.6, alpha: 1.0),
            UIColor.init(red: 118/255, green:  41/255, blue: 124/255, alpha: 1.0),
            UIColor.init(red:  61/255, green: 145/255, blue: 211/255, alpha: 1.0),
            UIColor.init(red: 149/255, green:  93/255, blue:  51/255, alpha: 1.0),
            UIColor.init(red:  60/255, green:  60/255, blue:  60/255, alpha: 1.0),
            UIColor.init(red: 153/255, green: 176/255, blue:  67/255, alpha: 1.0)
        ]
    }

}
