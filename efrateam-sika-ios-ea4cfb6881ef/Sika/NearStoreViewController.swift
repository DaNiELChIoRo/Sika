//
//  NearStoreViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 10/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import MapKit

enum NearStoreRootController {
    case map
    case product
}

class NearStoreViewController: BaseViewController {
    
    struct Constants {
        static let nearbyStoreMaxDistance: Int = 1000000
        static let regionRadius: CLLocationDistance = 10000

    }

    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var errorLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    var markers: Dictionary<Int, MKAnnotation> = [:]

    var rootController: NearStoreRootController = .map    
    var presenter: FindStoresPresenter!
    
    var selectedStore: ShopMarker?
    var stores: [ShopMarker] = []
    var district: String?
    var product: Product?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: PresenterType.findStoreViewController) as! FindStoresPresenter
        configureUI()
        if rootController == .map {
            getStoresByDistrict()
        } else {
            getStoresByProduct()
        }
    }
    
    func getStoresByProduct() {
        LocationManager.shared.getUserLocation() { [weak self] location in
            guard let strongSelf = self else { return }
            guard let productId = strongSelf.product?.id else { return }
            strongSelf.addIndicator()
            strongSelf.presenter.getStores(by: productId, lat: location.coordinate.latitude, lng: location.coordinate.longitude, distance: Constants.nearbyStoreMaxDistance) {
                success, response in
                DispatchQueue.main.async {
                    if let stores = response as? [ShopMarker] {
                        strongSelf.stores.append(contentsOf: stores)
                        strongSelf.updateModels()
                        strongSelf.centerMapOnLocation(location: location)
                        strongSelf.initMarkers()
                        strongSelf.removeIndicator()
                    } else {
                        strongSelf.removeIndicator()
                    }
                }
            }
        }
    }
    
    func getStoresByDistrict() {
        guard let district = district else { return }
        addIndicator()
        LocationManager.shared.getUserLocation() { [weak self] location in
            guard let strongSelf = self else { return }
            strongSelf.presenter.getStores(in: district) {
                success, response in
                DispatchQueue.main.async {
                    if let stores = response as? [ShopMarker] {
                        strongSelf.stores.append(contentsOf: stores)
                        strongSelf.updateModels()
                        strongSelf.centerMapOnLocation(location: location)
                        strongSelf.initMarkers()
                        strongSelf.removeIndicator()
                    } else {
                        strongSelf.removeIndicator()
                    }
                }
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
    
    func centerMapOnLocation(location: CLLocation) {
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(location.coordinate, Constants.regionRadius, Constants.regionRadius)
        mapView.setRegion(coordinateRegion, animated: false)
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

extension NearStoreViewController {
    
    func configureUI() {
        navigationItem.hidesBackButton = true
        let backButton = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Back), style: .plain, target: self, action: #selector(SearchViewController.back))
        navigationItem.leftBarButtonItem = backButton
        addHomeButton(nil)
        mapView.showsUserLocation = true
        mapView.showsBuildings = true
        if let location = LocationManager.shared.location {
            centerMapOnLocation(location: location)
        } else {
            LocationManager.shared.getUserLocation() { [weak self] location in
                guard let strongSelf = self else { return }
                strongSelf.centerMapOnLocation(location: location)
            }
        }
        errorLabel.isHidden = true
        tableView.isHidden = true
        configureTableview()
    }
    
    func updateModels() {
        if stores.isEmpty {
            errorLabel.isHidden = false
            tableView.isHidden = true
        } else {
            errorLabel.isHidden = true
            tableView.isHidden = false
            tableView.reloadData()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == SeguesId.storeDetailSegue, let vc = segue.destination as? StoreDetailViewController {
            vc.store = selectedStore
        }
    }
    
}

extension NearStoreViewController: UITableViewDelegate, UITableViewDataSource {
 
    func configureTableview() {
        tableView.register(UINib(nibName: AppConstants.Cells.NearStore, bundle: nil), forCellReuseIdentifier: AppConstants.Cells.NearStore)
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return stores.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AppConstants.Cells.NearStore) as! NearStoreTableViewCell
        cell.selectionStyle = .none
        cell.titleLabel.text = stores[indexPath.row].name ?? ""
        cell.addressLabel.text = stores[indexPath.row].address ?? ""
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedStore = stores[indexPath.row]
        performSegue(withIdentifier: SeguesId.storeDetailSegue, sender: self)
    }
    
}
