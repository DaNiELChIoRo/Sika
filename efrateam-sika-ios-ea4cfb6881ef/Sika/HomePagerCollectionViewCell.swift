//
//  HomePagerCollectionViewCell.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class HomePagerCollectionViewCell: UICollectionViewCell, ViewPagerDataSource, CellReusableIdentifierProtocol {

    var tapCallback: (() -> Void)?
    @IBOutlet weak var viewPager: ViewPager!
    var pages:[SliderItem] = []
    var images: [ImageRecord] = []
    let pendingOperations = PendingOperations()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.viewPager.dataSource = self
    }

    func setup(pages:[SliderItem]){
        self.pages = pages
        pages.forEach({
            page in
            if let stringUrl = page.image, let url = URL(string: stringUrl) {
                self.images.append(ImageRecord(name: "", url: url))
            } else {
                self.images.append(ImageRecord(name: "", url: nil))
            }
        })
        viewPager.reloadData()
        viewPager.pageControl.pageIndicatorTintColor = UIColor.gray
        viewPager.pageControl.currentPageIndicatorTintColor = UIColor.mainColor
        viewPager.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(viewPagerTapped(uiGesture:))))
    }
    
    func viewPagerTapped(uiGesture: UITapGestureRecognizer) {
        tapCallback?()
    }
    
    func numberOfItems(viewPager: ViewPager) -> Int {
        return pages.count
    }
    
    func viewAtIndex(viewPager: ViewPager, index: Int, view: UIView?) -> UIView {
        var mView: UIView
        if view != nil {
            view!.subviews.forEach { $0.removeFromSuperview() }
            mView = view!
        } else {
            mView = UIImageView(frame: viewPager.frame)
        }
        if let mView = mView as? UIImageView {
            let productImage = images[index]
            mView.image = productImage.image
            if productImage.state == .New {
                startDownloadForRecord(photoDetails: productImage, index: index)
            }
        }
        return mView
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
