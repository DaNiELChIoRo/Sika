//
//  StoreDetailViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 10/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import MapKit

class StoreDetailViewController: BaseViewController, MKMapViewDelegate {

    var store: ShopMarker?
    
    struct Constants {
        static let regionRadius: CLLocationDistance = 1000
        static let cornerRadius: CGFloat = 3
    }

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var callButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mapView.delegate = self
        configureUI()
    }
    
    @IBAction func callStore(_ sender: Any) {
        guard let phone = store?.phone else {
            // TODO: Show alert error
            return
        }
        if let url = URL(string: "tel://\(phone)"), UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        } else {
            // TODO: Show alert error
        }
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation is MKUserLocation
        {
            return nil
        }
        var annotationView = self.mapView.dequeueReusableAnnotationView(withIdentifier: "Pin")
        if annotationView == nil{
            annotationView = SikaAnnotationView(annotation: annotation, reuseIdentifier: "Pin")
            annotationView?.canShowCallout = false
        }else{
            annotationView?.annotation = annotation
        }
        annotationView?.image = UIImage(named: "SikaLogo")
        return annotationView
    }
    
    internal func mapView(_ mapView: MKMapView,
                          didSelect view: MKAnnotationView)
    {
        if view.annotation is MKUserLocation
        {
            return
        }
        let sikaAnnotation = view.annotation as! SikaAnnotation
        let views = Bundle.main.loadNibNamed("SikaCalloutView", owner: nil, options: nil)
        let calloutView = views?[0] as! SikaCalloutView
        calloutView.titleLabel.text = sikaAnnotation.title!
        calloutView.addressLabel.text = sikaAnnotation.subtitle!
        calloutView.storeButton.removeFromSuperview()
        calloutView.indicationsButton.setTitle("¿Cómo llegar?", for: .normal)
        calloutView.id = sikaAnnotation.id
        calloutView.delegate = self
        calloutView.center = CGPoint(x: view.bounds.size.width / 2, y: -calloutView.bounds.size.height*0.52)
        view.addSubview(calloutView)
        mapView.setCenter((view.annotation?.coordinate)!, animated: true)
    }
    
    func mapView(_ mapView: MKMapView, didDeselect view: MKAnnotationView) {
        if view.isKind(of: SikaAnnotationView.self)
        {
            for subview in view.subviews
            {
                subview.removeFromSuperview()
            }
        }
    }
    
}

extension StoreDetailViewController {
 
    func configureUI() {
        navigationItem.hidesBackButton = true
        let backButton = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Back), style: .plain, target: self, action: #selector(SearchViewController.back))
        navigationItem.leftBarButtonItem = backButton
        addHomeButton(nil)
        callButton.layer.cornerRadius = Constants.cornerRadius
        callButton.clipsToBounds = true
        
        guard let store = store , let name = store.name, let id = store.id else {
            return
        }
        let address = store.address ?? ""
        titleLabel.text = name
        addressLabel.text = address
        guard let lat = store.latitude, let lng = store.longitude else {
            return
        }
        let coordinate = CLLocationCoordinate2D(latitude: NSString(string: lat).doubleValue , longitude: NSString(string: lng).doubleValue)
        centerMapOnLocation(coordinate: coordinate)
        let annotation = SikaAnnotation(id: id, identifier: String(id), title: name, subtitle: address, coordinate: coordinate, store: store)
        mapView.addAnnotation(annotation)
    }
    
    func centerMapOnLocation(coordinate: CLLocationCoordinate2D) {
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(coordinate, Constants.regionRadius, Constants.regionRadius)
        mapView.setRegion(coordinateRegion, animated: true)
    }
    
    @IBAction func zoomIn(_ sender: Any) {
        var region: MKCoordinateRegion = mapView.region
        region.span.latitudeDelta /= 2.0
        region.span.longitudeDelta /= 2.0
        mapView.setRegion(region, animated: false)
    }
    
    @IBAction func zoomOut(_ sender: Any) {
        var region: MKCoordinateRegion = mapView.region
        region.span.latitudeDelta = min(region.span.latitudeDelta * 2.0, 180.0)
        region.span.longitudeDelta = min(region.span.longitudeDelta * 2.0, 180.0)
        mapView.setRegion(region, animated: true)
    }
    
}

extension StoreDetailViewController: MapCalloutProtocol {
    
    func didTapStore(id: Int) {
    
    }
    
    func didTapIndications(id: Int) {
        guard let lat = store?.latitude, let lng = store?.longitude else {
            return
        }
        let coordinate = CLLocationCoordinate2D(latitude: NSString(string: lat).doubleValue , longitude: NSString(string: lng).doubleValue)
        let name = store?.name ?? ""
        LocationManager.shared.showRoute(destination: coordinate, name: name)
    }
    
}
