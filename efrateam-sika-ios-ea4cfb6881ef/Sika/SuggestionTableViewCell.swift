//
//  SuggestionTableViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SuggestionTableViewCell: UITableViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var card: UIView!
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var title: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        card.layer.cornerRadius = 3.5
        card.layer.shadowColor = UIColor.black.cgColor
        card.layer.shadowOpacity = 1.5
        card.layer.shadowOffset = CGSize.zero
        card.layer.shadowRadius = 1.5
    }
    
}
