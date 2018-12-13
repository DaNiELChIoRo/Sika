//
//  SearchViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/17/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import NVActivityIndicatorView

enum ProductsDisplayed: String {
    case all = "All"
    case filtered = "Filtered"
}

enum RootController {
    case home
    case menu
}

class SearchViewController: BaseCollectionViewController {

    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var tableView: UITableView!
    var presenter: TaxonomiesPresenter!
    var categories: [TaxonomyCategory] = []
    var filteredProducts: [Product] = []
    var images: [ImageRecord] = []
    var requestType: ProductsDisplayed = .all
    var rootControllerCase: RootController = .menu
    
    let alertTitleText = "ALERT_TITLE".localized()
    let emptySearchText = "ALERT_SEARCH_EMPTY_RESULT".localized()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: .searchViewController) as! TaxonomiesPresenter
        configureUI()
    }
    
    override func back() {
        if requestType == .filtered {
            requestType = .all
            tableView.reloadData()
        } else {
            _ = navigationController?.popViewController(animated: true)
        }
    }
    
    override func reloadIndexPaths(indexPaths: [IndexPath]) {
        tableView.reloadRows(at: indexPaths, with: .none)
    }
    
}

extension SearchViewController: UITextFieldDelegate, UISearchBarDelegate {

    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        guard let query = searchBar.text else { return }
        addIndicator()
        presenter.getProducts(search: query, page: 0) {
            success, response in
            
            guard !response.isEmpty && success else {
                DispatchQueue.main.async { [weak self] in
                    if let strongSelf = self {
                        strongSelf.removeIndicator()
                        strongSelf.presentAlert(title: strongSelf.alertTitleText, message: strongSelf.emptySearchText)
                    }
                }
                return
            }
            
            if let products = response as? [Product] {
                let sortedProducts = products.sorted { $0.name!.localizedCaseInsensitiveCompare($1.name!) == ComparisonResult.orderedAscending }
                self.filteredProducts.removeAll()
                self.images.removeAll()
                sortedProducts.forEach({
                    product in
                    if let stringUrl = product.photo?.getLowestThumbnailResolution()?.url, let url = URL(string: stringUrl) {
                        self.images.append(ImageRecord(name: "", url: url))
                    }
                })
                self.filteredProducts.append(contentsOf: sortedProducts)
                DispatchQueue.main.async { [weak self] in
                    if let strongSelf = self {
                        strongSelf.requestType = .filtered
                        strongSelf.tableView.reloadData()
                        strongSelf.removeIndicator()
                    }
                }
            } else {
                DispatchQueue.main.async { [weak self] in
                    if let strongSelf = self {
                        strongSelf.removeIndicator()
                    }
                }
            }
        }
        searchBar.resignFirstResponder()
    }
    
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        return true
    }
    
}

extension SearchViewController: UITableViewDelegate, UITableViewDataSource {

    func configureUI() {
        title = "Buscar productos..."
        navigationController?.navigationBar.tintColor = UIColor.white
        
        tableView.register(UINib(nibName: AppConstants.Cells.SearchProduct, bundle: nil), forCellReuseIdentifier: AppConstants.Cells.SearchProduct)
        tableView.register(UINib(nibName: AppConstants.Cells.SearchFilteredProduct, bundle: nil), forCellReuseIdentifier: AppConstants.Cells.SearchFilteredProduct)
        tableView.separatorStyle = .none
        tableView.alwaysBounceVertical = false
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = view.backgroundColor
        tableView.showsVerticalScrollIndicator = false
        
        searchBar.delegate = self
        searchBar.keyboardType = .asciiCapable
        searchBar.returnKeyType = .done
        
        if rootControllerCase == .menu {
            addNavButton()
        } else {
            navigationItem.hidesBackButton = true
            let backButton = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Back), style: .plain, target: self, action: #selector(SearchViewController.back))
            navigationItem.leftBarButtonItem = backButton
        }
        
        addIndicator()
        presenter.getProductsByCategory() {
            success, response in
            if let categories = response as? [TaxonomyCategory] {
                self.categories.append(contentsOf: categories.sorted { $0.value!.localizedCaseInsensitiveCompare($1.value!) == ComparisonResult.orderedAscending })
                DispatchQueue.main.async { [weak self] in
                    if let strongSelf = self {
                        strongSelf.tableView.reloadData()
                        strongSelf.removeIndicator()
                    }
                }
            } else {
                DispatchQueue.main.async { [weak self] in
                    if let strongSelf = self {
                        strongSelf.removeIndicator()
                    }
                }
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if requestType == .all {
            let category = categories[indexPath.row]
            addIndicator()
            presenter.getProducts(id: category.id!) {
                success, response in
                
                guard !response.isEmpty && success else {
                    DispatchQueue.main.async { [weak self] in
                        if let strongSelf = self {
                            strongSelf.removeIndicator()
                            strongSelf.presentAlert(title: strongSelf.alertTitleText, message: strongSelf.emptySearchText)
                        }
                    }
                    return
                }
                
                if var products = response as? [Product] {
                    products = products.sorted { $0.name!.localizedCaseInsensitiveCompare($1.name!) == ComparisonResult.orderedAscending }
                    
                    self.filteredProducts.removeAll()
                    self.images.removeAll()
                    
                    products.forEach({
                        product in
                        if let stringUrl = product.photo?.getLowestThumbnailResolution()?.url, let url = URL(string: stringUrl) {
                            self.images.append(ImageRecord(name: "", url: url))
                        } else {
                            self.images.append(ImageRecord(name: "", url: nil))
                        }
                    })
                    
                    self.filteredProducts.append(contentsOf: products)
                    DispatchQueue.main.async { [weak self] in
                        if let strongSelf = self {
                            strongSelf.requestType = .filtered
                            strongSelf.tableView.reloadData()
                            strongSelf.removeIndicator()
                        }
                    }
                } else {
                    DispatchQueue.main.async { [weak self] in
                        if let strongSelf = self {
                            strongSelf.removeIndicator()
                            strongSelf.presentAlert(title: strongSelf.alertTitleText, message: strongSelf.emptySearchText)
                        }
                    }
                }
            }

        } else {
            let product = filteredProducts[indexPath.row]
            let vc = storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Product) as! ProductViewController
            vc.productId = product.id!
            vc.product = product
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if requestType == .all {
            return categories.count
        }
        return filteredProducts.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if requestType == .all {
            return 65.0
        }
        return 85.0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch requestType {
        case .all:
            let cell = tableView.dequeueReusableCell(withIdentifier: AppConstants.Cells.SearchProduct) as! SearchProductTableViewCell
            if let value = categories[indexPath.row].value {
                cell.titleLabel.text = value
            }
            cell.selectionStyle = .none
            return cell
        case .filtered:
            let productImage = images[indexPath.row]
            let product = filteredProducts[indexPath.row]
            
            let cell = tableView.dequeueReusableCell(withIdentifier: AppConstants.Cells.SearchFilteredProduct) as! SearchFilteredProductTableViewCell
            cell.backgroundColor = tableView.backgroundColor
            
            if let name = product.name{
                cell.label.text = name
            }
            
            cell.img.image = productImage.image
            if productImage.state == .New {
                startDownloadForRecord(photoDetails: productImage, indexPath: indexPath)
            }
            cell.selectionStyle = .none
            return cell
        }
    }

}
