//
//  HomeCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class HomeCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var card: UIView!
    @IBOutlet weak var categoryLabel: UILabel!
    @IBOutlet weak var image: UIImageView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        card.layer.cornerRadius = 3.5
        card.layer.shadowColor = UIColor.black.cgColor
        card.layer.shadowOpacity = 1.5
        card.layer.shadowOffset = CGSize.zero
        card.layer.shadowRadius = 1.5
        image.layer.cornerRadius = 3.5
    }

}
