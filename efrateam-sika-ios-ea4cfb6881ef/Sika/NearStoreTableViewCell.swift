//
//  NearStoreTableViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 10/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class NearStoreTableViewCell: UITableViewCell {

    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var mView: UIView!
    
    let cornerRadius: CGFloat = 5.0
    
    override func awakeFromNib() {
        super.awakeFromNib()
        mView.layer.borderWidth = 1.0
        mView.layer.borderColor = UIColor.darkGray.cgColor
        mView.layer.cornerRadius = cornerRadius
        mView.clipsToBounds = true
    }

}
