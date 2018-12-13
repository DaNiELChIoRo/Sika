//
//  LocationManager.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import CoreLocation
import MapKit

typealias LocationClosure = ((CLLocation) -> Void)

class LocationManager: NSObject, CLLocationManagerDelegate {
    
    static let shared = LocationManager()
    var location: CLLocation?
    
    private var locationManager: CLLocationManager?
    var completion: LocationClosure?
    
    func getUserLocation(completion: @escaping LocationClosure) {
        self.completion = completion
        initLocationManager()
    }
    
    func initLocationManager() {
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.requestAlwaysAuthorization()
        locationManager?.desiredAccuracy = kCLLocationAccuracyBest
        locationManager?.distanceFilter = 1000
        locationManager?.startUpdatingLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if locations.count > 0{
            location = locations.last!
            completion?(location!)
        }
    }
    
    func showRoute(destination: CLLocationCoordinate2D, name: String) {
        let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: destination, addressDictionary: nil))
        mapItem.name = name
        mapItem.openInMaps(launchOptions: [MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving])
    }
}
