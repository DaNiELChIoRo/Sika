//
//  PromotionsViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

class PromotionsViewController: BaseViewController {

    let pendingOperations = PendingOperations()

    @IBOutlet weak var viewPager: ViewPager!
    var presenter: TaxonomiesPresenter!
    var promotions: [Promotion] = []
    var images: [ImageRecord] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: .searchViewController) as! TaxonomiesPresenter
        configureUI()
    }
    
}

extension PromotionsViewController {

    func configureUI() {
        title = "Promociones"
        addNavButton()
        addHomeButton(nil)

        viewPager.dataSource = self
        viewPager.pageControl.pageIndicatorTintColor = UIColor.gray
        viewPager.pageControl.currentPageIndicatorTintColor = UIColor.mainColor
        
        addIndicator()
        presenter.getPromotions() {
            success, response in
            DispatchQueue.main.async { [weak self] in
                guard let strongSelf = self else { return }
                guard let promotions = response as? [Promotion] else {
                    strongSelf.removeIndicator()
                    return
                }
                for promotion in promotions {
                    strongSelf.images.append(ImageRecord(name: promotion.title ?? "", url: URL(string: promotion.image ?? "") ?? nil))
                }
                strongSelf.promotions.removeAll()
                strongSelf.promotions.append(contentsOf: promotions)
                strongSelf.removeIndicator()
                strongSelf.viewPager.reloadData()
            }
        }
    }
    
}

extension PromotionsViewController: ViewPagerDataSource {
    
    func numberOfItems(viewPager: ViewPager) -> Int {
        return promotions.count
    }
    
    func viewAtIndex(viewPager: ViewPager, index: Int, view: UIView?) -> UIView {
        var mView = view
        let promotion = promotions[index]
        if mView == nil {
            mView = PromotionPage.initFromNib()
        }
        if let promotionView = mView as? PromotionPage {
            let promotionImage = images[index]
            if promotionImage.state == .New {
                startDownloadForRecord(photoDetails: promotionImage, index: index)
            }
            promotionView.setup(text: promotion.description ?? "", image: promotionImage.image)
        }
        return mView!
    }
    
    func didSelectedItem(index: Int) {
        
    }
    
    func startDownloadForRecord(photoDetails: ImageRecord, index: Int){
        guard pendingOperations.downloadsQueued[index] == nil else {
            return
        }
        let downloader = ImageDownloader(imageRecord: photoDetails)
        downloader.completionBlock = {
            if downloader.isCancelled {
                return
            }
            DispatchQueue.main.async {
                self.pendingOperations.downloadsQueued.removeValue(forKey: index)
                self.viewPager.reloadViews(index: index)
            }
        }
        pendingOperations.downloadsQueued[index] = downloader
        pendingOperations.downloadQueue.addOperation(downloader)
    }
}
