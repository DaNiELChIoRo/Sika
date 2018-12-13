//
//  ProductHeaderCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class ProductHeaderCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var title: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.title.textColor = UIColor.mainColor
    }

}
