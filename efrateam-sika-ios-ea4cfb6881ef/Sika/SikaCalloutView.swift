//
//  SikaCalloutView.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

protocol MapCalloutProtocol: class {
    func didTapStore(id: Int)
    func didTapIndications(id: Int)
}

class SikaCalloutView: UIView {

    weak var delegate: MapCalloutProtocol?
    
    @IBOutlet weak var indicationsButton: UIButton!
    @IBOutlet weak var storeButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    var id: Int = 0
    
    @IBAction func storeButtonTouchUpInside(_ sender: Any) {
        delegate?.didTapStore(id: id)
    }
    
    @IBAction func indicationsButtonTouchUpInside(_ sender: Any) {
        delegate?.didTapIndications(id: id)
    }
    
}
