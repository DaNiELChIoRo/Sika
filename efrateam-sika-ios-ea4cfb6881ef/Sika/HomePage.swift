//
//  HomePage.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class HomePage: UIView {

    @IBOutlet weak var imageView: UIImageView!
    
    class func initFromNib() -> HomePage? {
        guard let className = NSStringFromClass(self).components(separatedBy: ".").last else { return nil }
        let nibFile = UINib(nibName: className, bundle: nil)
        guard let homePage = nibFile.instantiate(withOwner: self, options: nil).first as? HomePage else { return nil }
        return homePage
    }
    
}
