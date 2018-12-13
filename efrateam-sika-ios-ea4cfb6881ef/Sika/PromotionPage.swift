//
//  PromotionPage.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/8/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class PromotionPage: UIView {

    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var promotionsTextview: UITextView!
    @IBOutlet weak var promotionsImage: UIImageView!
    
    class func initFromNib() -> PromotionPage? {
        guard let className = NSStringFromClass(self).components(separatedBy: ".").last else { return nil }
        let nibFile = UINib(nibName: className, bundle: nil)
        guard let promotionPage = nibFile.instantiate(withOwner: self, options: nil).first as? PromotionPage else { return nil }
        return promotionPage
    }
    
    func setup(text: String, image: UIImage?) {
        promotionsTextview.attributedText = text.htmlAttributedString
        promotionsImage.image = image
    }
    
}
