//
//  MenuTableViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class MenuTableViewCell: UITableViewCell, CellReusableIdentifierProtocol {
    
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var icon: UIImageView!

    override func setSelected(_ selected: Bool, animated: Bool) {
        if selected {
            setTint(.menuIconRed)
        } else {
            setTint(.menuIconGray)
        }
    }

    func setTint(_ color: UIColor) {
        title.textColor = color
        if let image = icon.image{
            icon.image = image.withRenderingMode(.alwaysTemplate)
            icon.tintColor = color
        }
    }

}
