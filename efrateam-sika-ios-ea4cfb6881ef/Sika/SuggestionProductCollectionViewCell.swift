//
//  SuggestionProductCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/28/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SuggestionProductCollectionViewCell: UICollectionViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var image: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
