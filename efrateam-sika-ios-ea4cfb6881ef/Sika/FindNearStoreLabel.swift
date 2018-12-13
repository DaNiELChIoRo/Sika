//
//  FindNearStoreLabel.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 1/13/18.
//  Copyright © 2018 Sika. All rights reserved.
//

import Foundation
import UIKit

class FindNearStoreLabel: UILabel {
    
    let padding = UIEdgeInsets(top: 5, left: 40, bottom: 5, right: 8)
    override func drawText(in rect: CGRect) {
        super.drawText(in: UIEdgeInsetsInsetRect(rect, padding))
    }
    
    override var intrinsicContentSize : CGSize {
        let superContentSize = super.intrinsicContentSize
        let width = superContentSize.width + padding.left + padding.right
        let heigth = superContentSize.height + padding.top + padding.bottom
        return CGSize(width: width, height: heigth)
    }
    
}
