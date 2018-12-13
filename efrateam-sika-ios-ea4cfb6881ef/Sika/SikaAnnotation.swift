//
//  SikaAnnotation.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import MapKit
import CoreLocation

class SikaAnnotation: NSObject, MKAnnotation {

    let id: Int
    let identifier: String
    let title: String?
    let subtitle: String?
    let coordinate: CLLocationCoordinate2D
    let store: ShopMarker
    
    init(id: Int, identifier: String, title: String, subtitle: String, coordinate: CLLocationCoordinate2D, store: ShopMarker)
    {
        self.id = id
        self.identifier = identifier
        self.title = title
        self.subtitle = subtitle
        self.coordinate = coordinate
        self.store = store
        super.init()
    }
    

}
