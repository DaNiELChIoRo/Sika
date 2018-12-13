//
//  StepsHeaderCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class StepsHeaderCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var image: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
