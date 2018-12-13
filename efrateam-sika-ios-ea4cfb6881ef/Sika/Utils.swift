//
//  Utils.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

class Utils {

    static let standardSize: CGSize = CGSize(width: 375.0, height: 667.0)
    
    static func getScreenSize() -> CGSize{
        return UIScreen.main.bounds.size
    }
    
    static func transformHeight(height: CGFloat) -> CGFloat {
        return height * getScreenSize().height / standardSize.height
    }
    
    static func transformWidth(width: CGFloat) -> CGFloat {
        return width * getScreenSize().width / standardSize.width
    }

}
