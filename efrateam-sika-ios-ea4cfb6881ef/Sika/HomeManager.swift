//
//  HomeManager.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
class HomeManager{
    
    static let shared = HomeManager()
    
    func getPages() -> [PagerItem] {
        return [
            PagerItem(productImage: "Sealer", image: "SealerIcon", info:"Sellado de juntas y adhesivos elásticos"),
            PagerItem(productImage: "Sealer", image: "SealerIcon", info:"Sellado de juntas y adhesivos elásticos"),
            PagerItem(productImage: "Sealer", image: "SealerIcon", info:"Sellado de juntas y adhesivos elásticos"),
            PagerItem(productImage: "Sealer", image: "SealerIcon", info:"Sellado de juntas y adhesivos elásticos"),
            PagerItem(productImage: "Sealer", image: "SealerIcon", info:"Sellado de juntas y adhesivos elásticos")
        ]
    }
    
    func getHomeItems() -> [HomeItem] {
        return [
            HomeItem(image: "", category: "Aditivos", purpose:"para mortero y concreto"),
            HomeItem(image: "", category: "Selladores", purpose:"para grietas y juntas"),
            HomeItem(image: "", category: "Impermeabilizantes", purpose:"para techos, cubiertas y muros"),
            HomeItem(image: "", category: "Morteros", purpose:"para decoración y reparación"),
            HomeItem(image: "", category: "Pisos y Recubrimientos", purpose:"para decoración y reparación"),
            HomeItem(image: "", category: "Adhesivos", purpose:"Epóxicos y anclajes")
        ]
    }
    
}
