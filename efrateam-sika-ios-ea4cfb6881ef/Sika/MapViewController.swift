//
//  MapViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/27/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class MapViewController: BaseViewController, MKMapViewDelegate {
    
    struct Constants {
        static let appleMapsUrl = "http://maps.apple.com/?saddr=%@"
        static let annotationFont = UIFont(name: Font.FontName.OpenSansRegular, size: 16.0)
    }
    
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var closestStoreButton: UIButton!
    
    let regionRadius: CLLocationDistance = 1000
    var location: CLLocation?
    
    var mapInitialized = false
    var presenter: FindStoresPresenter!
    var stores: [ShopMarker] = []

    var markers: Dictionary<Int, MKAnnotation> = [:]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        presenter = PresenterLocator.getPresenter(ofType: PresenterType.findStoreViewController) as! FindStoresPresenter
        mapView.showsUserLocation = true
        mapView.showsBuildings = true
        mapView.delegate = self
        closestStoreButton.layer.cornerRadius = 3.0
        closestStoreButton.clipsToBounds = true
        initMap()
    }

    func centerMapOnLocation(location: CLLocation) {
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(location.coordinate, regionRadius, regionRadius)
        mapView.setRegion(coordinateRegion, animated: true)
    }
    
    func initMap() {
        guard !mapInitialized else { return }
        if let location = LocationManager.shared.location {
            self.location = location
            centerMapOnLocation(location: location)
            mapInitialized = !mapInitialized
            getNearbyStores(location: location)
        } else  {
            LocationManager.shared.getUserLocation() { [weak self] location in
                guard let strongSelf = self else { return }
                strongSelf.location = location
                strongSelf.centerMapOnLocation(location: location)
                strongSelf.mapInitialized = !strongSelf.mapInitialized
                strongSelf.getNearbyStores(location: location)
            }
        }
    }
    
    func getNearbyStores(location: CLLocation) {
        addIndicator()
        presenter.getNearbyStores(lat: location.coordinate.latitude, lng: location.coordinate.longitude, page: 1) {
            success, response in
            DispatchQueue.main.async { [weak self] in
                guard let strongSelf = self else { return }
                if let stores = response as? [ShopMarker] {
                    strongSelf.stores.append(contentsOf: stores)
                    strongSelf.initMarkers()
                    strongSelf.removeIndicator()
                } else {
                    strongSelf.removeIndicator()
                }
            }
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
        calloutView.storeButton.setTitle("Ver detalles", for: .normal)
        calloutView.indicationsButton.setTitle("¿Cómo llegar?", for: .normal)
        calloutView.id = sikaAnnotation.id
        calloutView.delegate = self
        calloutView.center = CGPoint(x: view.bounds.size.width / 2, y: -calloutView.bounds.size.height * 0.52)
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
    
    func initMarkers() {
        mapView.removeAnnotations(Array(markers.values))
        markers.removeAll()
        for store in stores {
            if let lat = store.latitude, let lng = store.longitude, let id = store.id, let title = store.name {
                let coordinate = CLLocationCoordinate2D(latitude: NSString(string: lat).doubleValue , longitude: NSString(string: lng).doubleValue)
                let annotation = SikaAnnotation(id: id, identifier: String(id), title: title, subtitle: store.address ?? "", coordinate: coordinate, store: store)
                mapView.addAnnotation(annotation)
                markers[id] = annotation
            }
        }
    }
    
    func showRouteOnMap(destination: CLLocationCoordinate2D) {
        guard let location = location else { return }
        let overlays = mapView.overlays
        mapView.removeOverlays(overlays)
        addIndicator()
        let request = MKDirectionsRequest()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: location.coordinate, addressDictionary: nil))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: destination, addressDictionary: nil))
        request.requestsAlternateRoutes = true
        request.transportType = .automobile
        let directions = MKDirections(request: request)
        directions.calculate(completionHandler: { [unowned self] (response, error) in
            self.removeIndicator()
            guard let unwrappedResponse = response else { return }
            if (unwrappedResponse.routes.count > 0) {
                self.mapView.add(unwrappedResponse.routes[0].polyline)
                self.mapView.setVisibleMapRect(unwrappedResponse.routes[0].polyline.boundingMapRect, animated: true)
            }
        })
    }
    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        if overlay is MKPolyline {
            let polylineRenderer = MKPolylineRenderer(overlay: overlay)
            polylineRenderer.strokeColor = UIColor.mainColor
            polylineRenderer.lineWidth = 3
            return polylineRenderer
        }
        return MKOverlayRenderer()
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
    
    @IBAction func showClosestStore(_ sender: Any) {
        addIndicator()
        findNearestStore() { annotation in
            DispatchQueue.main.async { [weak self] in
                guard let strongSelf = self else { return }
                strongSelf.removeIndicator()
                guard let annotation = annotation else { return }
                let annotations = [strongSelf.mapView.userLocation, annotation]
                strongSelf.mapView.showAnnotations(annotations, animated: true)
            }
        }
    }
    
    func findNearestStore(completion: ((MKAnnotation?) -> Void)?) {
        let pins = mapView.annotations.filter({ !$0.isKind(of: MKUserLocation.self) })
        let currentLocation = mapView.userLocation.location!
        
        DispatchQueue.global(qos: .background).async {
            let nearestPin: MKAnnotation? = pins.reduce((CLLocationDistanceMax, nil)) { (nearest, pin) in
                let coord = pin.coordinate
                let loc = CLLocation(latitude: coord.latitude, longitude: coord.longitude)
                let distance = currentLocation.distance(from: loc)
                return distance < nearest.0 ? (distance, pin) : nearest
                }.1
            completion?(nearestPin)
        }
    }
    
}

extension MapViewController: MapCalloutProtocol {
    
    func didTapStore(id: Int) {
        let vc = storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.StoreDetails) as! StoreDetailViewController
        vc.store = (markers[id] as! SikaAnnotation).store
        navigationController?.pushViewController(vc, animated: true)
    }
    
    func didTapIndications(id: Int) {
        let annotation = markers[id]
        guard let coordinate = annotation?.coordinate else { return }
        let name = annotation?.title ?? ""
        LocationManager.shared.showRoute(destination: coordinate, name: name ?? "")
    }
    
}
