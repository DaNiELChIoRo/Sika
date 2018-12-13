//
//  StepCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class StepCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var stepDescription: UITextView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
