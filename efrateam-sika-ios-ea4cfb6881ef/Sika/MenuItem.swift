//
//  MenuItem.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol MenuComponents {
    var icon: String? { get }
    var title: String? { get }
}

struct MenuItem: MenuComponents {
    var icon: String?
    var title: String?
}

struct MenuItems {
    var items: [MenuItem]?
}
