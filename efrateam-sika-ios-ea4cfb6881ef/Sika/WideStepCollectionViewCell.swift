//
//  WideStepCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 3/17/18.
//  Copyright © 2018 Sika. All rights reserved.
//

import UIKit

class WideStepCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {
    
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var stepDescription: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
