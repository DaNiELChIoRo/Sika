//
//  MenuManager.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
class MenuManager{

    static let shared = MenuManager()
    let menuItems: [MenuItem] = [
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuHome, title: "Inicio"),
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuLocation, title: "Buscar tiendas"),
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuSearch, title: "Buscar productos"),
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuOffer, title: "Promociones"),
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuEmail, title: "Contáctanos"),
        MenuItem(icon: AppConstants.Icons.MenuIcons.MenuGlossary, title: "Glosario")
    ]
    
}
