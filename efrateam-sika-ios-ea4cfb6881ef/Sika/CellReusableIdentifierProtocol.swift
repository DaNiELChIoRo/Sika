//
//  CellReusableIdentifierProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/14/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit
protocol CellReusableIdentifierProtocol {}
extension CellReusableIdentifierProtocol where Self: UIView {
    static var reuseIdentifier: String {
        return String(describing: self)
    }
}
