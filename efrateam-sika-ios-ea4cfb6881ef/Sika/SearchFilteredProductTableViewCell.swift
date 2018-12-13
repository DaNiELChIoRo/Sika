//
//  SearchFilteredProductTableViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SearchFilteredProductTableViewCell: UITableViewCell {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var separator: UIView!
    @IBOutlet weak var label: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        containerView.layer.cornerRadius = 5.0
        containerView.clipsToBounds = true
    }
 
    override func prepareForReuse() {
        img.image = nil
    }
    
}
