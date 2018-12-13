//
//  HomeItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol HomeComponents {
    var image:String? { get }
    var category:String? { get }
    var purpose:String? { get }
}

struct HomeItem:HomeComponents{
    var image:String?
    var category:String?
    var purpose:String?
}
