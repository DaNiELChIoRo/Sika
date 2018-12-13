//
//  PagerItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol PagerItemComponents{
    var productImage: String? { get }
    var image: String? { get }
    var info: String? { get }
}

struct PagerItem: PagerItemComponents{
    var productImage: String?
    var image: String?
    var info: String?
}
