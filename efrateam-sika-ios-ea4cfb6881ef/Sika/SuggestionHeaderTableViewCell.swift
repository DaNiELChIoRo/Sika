//
//  SuggestionHeaderTableViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SuggestionHeaderTableViewCell: UITableViewCell, CellReusableIdentifierProtocol {

    @IBOutlet weak var section: UILabel!
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var title: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
