//
//  UIColor.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

extension UIColor{

    class var mainColor:UIColor {
        return UIColor(displayP3Red: 233/255, green: 178/255, blue: 71/255, alpha: 1.0)
    }
    
    class var scondaryMainColor: UIColor {
        return UIColor(displayP3Red: 245/255, green: 174/255, blue: 70/255, alpha: 1.0)
    }
    
    class var sikaGray:UIColor {
        return UIColor(displayP3Red: 145/255, green: 145/255, blue: 145/255, alpha: 1.0)
    }
    
    class var darkGray:UIColor {
        return UIColor(displayP3Red: 40/255, green: 40/255, blue: 40/255, alpha: 1.0)
    }
    
    class var menuIconGray: UIColor {
        return UIColor(displayP3Red: 133/255, green: 133/255, blue: 133/255, alpha: 1.0)
    }
    
    class var menuIconRed: UIColor {
        return UIColor(displayP3Red: 210/255, green: 40/255, blue: 47/255, alpha: 1.0)
    }

    convenience init(hex: String) {
        let scanner = Scanner(string: hex)
        scanner.scanLocation = 0
        
        var rgbValue: UInt64 = 0
        
        scanner.scanHexInt64(&rgbValue)
        
        let r = (rgbValue & 0xff0000) >> 16
        let g = (rgbValue & 0xff00) >> 8
        let b = rgbValue & 0xff
        
        self.init(
            red: CGFloat(r) / 0xff,
            green: CGFloat(g) / 0xff,
            blue: CGFloat(b) / 0xff, alpha: 1
        )
    }
    
}
