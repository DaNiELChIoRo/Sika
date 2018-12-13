//
//  ProductViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class ProductViewController: BaseCollectionViewController {

    @IBOutlet weak var buttonIcon: UIImageView!
    @IBOutlet weak var buttonLabel: FindNearStoreLabel!
    @IBOutlet weak var findStoresButton: UIButton!
    @IBOutlet weak var viewPager: ViewPager!
    @IBOutlet weak var productName: UILabel!
    @IBOutlet weak var productDescription: UILabel!
    
    var productId: Int = 0
    var product: Product?
    var pages: [PagerItem] = []
    var images: [ImageRecord] = []
    
    let relatedImagesOperation = PendingOperations()
    var relatedImages: [ImageRecord] = []
    
    var presenter: TaxonomiesPresenter!
    
    struct Constants {
        static let cornerRadius: CGFloat = 5
    }

    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: .searchViewController) as! TaxonomiesPresenter
        configureUI()
    }
    
    override func reloadIndexes(indexes: [Int]) {
        viewPager.reloadViews(index: indexes.first!)
    }
    
    @IBAction func findNearStores(_ sender: Any) {
        showNearbyStores()
    }
    
}

extension ProductViewController {

    func configureUI() {
        navigationItem.hidesBackButton = true
        let backButton = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Back), style: .plain, target: self, action: #selector(SearchViewController.back))
        navigationItem.leftBarButtonItem = backButton
        addHomeButton(nil)
        buttonLabel.layer.cornerRadius = Constants.cornerRadius
        buttonLabel.clipsToBounds = true
        buttonIcon.image = buttonIcon.image?.withRenderingMode(.alwaysTemplate)
        buttonIcon.tintColor = .white
        
        addIndicator()
        presenter.getProductInfo(id: productId, completion: {
            success, response in
            
            guard success, let product = response.first as? Product else {
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.removeIndicator()
                }
                return
            }
            self.product = product
            if let name = product.name, var pDescription = product.details {
                while let rangeToReplace = pDescription.range(of: "\n") {
                    pDescription.replaceSubrange(rangeToReplace, with: " ")
                }
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.productName.text = name
                    strongSelf.productDescription.text = pDescription
                    strongSelf.productDescription.sizeToFit()
                }
            }
            if let thumbnails = product.photo?.thumbnails {
                thumbnails.forEach({
                    thumbnail in
                    if let stringUrl = thumbnail.url, let url = URL(string: stringUrl) {
                        self.images.append(ImageRecord(name: "", url: url))
                    }
                })
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.viewPager.pageControl.pageIndicatorTintColor = UIColor.gray
                    strongSelf.viewPager.pageControl.currentPageIndicatorTintColor = UIColor.mainColor
                    strongSelf.viewPager.dataSource = strongSelf
                    strongSelf.removeIndicator()
                }
            }
        })
    }
    
    func showNearbyStores() {
        let storyboard = UIStoryboard(name: "Stores", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "NearStoreViewController") as? NearStoreViewController {
            vc.rootController = .product
            vc.product = product
            navigationController?.pushViewController(vc, animated: true)
        }
    }

}

extension ProductViewController : ViewPagerDataSource {
    
    func numberOfItems(viewPager: ViewPager) -> Int {
        return images.count
    }
    
    func viewAtIndex(viewPager: ViewPager, index: Int, view: UIView?) -> UIView {
        var mView: UIView
        if view != nil {
            view!.subviews.forEach { $0.removeFromSuperview() }
            mView = view!
        } else {
            mView = UIView(frame: viewPager.frame)
        }
        let image = UIImageView(frame: CGRect(x: 0, y: 0, width: Utils.transformHeight(height: 150.0), height: Utils.transformWidth(width: 150.0)))
        image.center = viewPager.center
        
        let productImage = images[index]
        image.image = productImage.image
        if productImage.state == .New {
            startDownloadForRecord(photoDetails: productImage, index: index)
        }
        mView.addSubview(image)
        return mView
    }
    
}
