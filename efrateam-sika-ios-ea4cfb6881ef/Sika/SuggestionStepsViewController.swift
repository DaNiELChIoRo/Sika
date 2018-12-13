//
//  SuggestionStepsViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SuggestionStepsViewController: BaseCollectionViewController {

    var headerImage: ImageRecord!
    var demoImage: ImageRecord!
    var productImages: [ImageRecord] = []
    var stepImages: [ImageRecord] = []
    
    var suggestion: Suggestion?
    var problemItem: ProblemItem?
    @IBOutlet weak var collectionView: UICollectionView!
    var presenter: TaxonomiesPresenter!

    var textHeights: [CGFloat] = []
    
    struct Constants {
        static let stepRestrictedWidth: CGFloat = Utils.getScreenSize().width / 2 - 30.0
        static let textFont: UIFont = UIFont(name: Font.FontName.OpenSansLight, size: 14.0)!
        static let defaultCellHeight: CGFloat = (Utils.getScreenSize().width * 1.65) - 30.0
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = suggestion?.title ?? ""
        presenter = PresenterLocator.getPresenter(ofType: .searchViewController) as! TaxonomiesPresenter

        collectionView.register( UINib(nibName: StepCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: StepCollectionViewCell.reuseIdentifier)
        collectionView.register( UINib(nibName: StepsHeaderCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: StepsHeaderCollectionViewCell.reuseIdentifier)
        collectionView.register( UINib(nibName: ApplicationHeaderCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: ApplicationHeaderCollectionViewCell.reuseIdentifier)
        collectionView.register( UINib(nibName: ProductHeaderCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: ProductHeaderCollectionViewCell.reuseIdentifier)
        collectionView.register( UINib(nibName: SuggestionProductCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: SuggestionProductCollectionViewCell.reuseIdentifier)
        collectionView.register( UINib(nibName: SuggestionExampleCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: SuggestionExampleCollectionViewCell.reuseIdentifier)
        collectionView.register(UINib(nibName: StepsTitleCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: StepsTitleCollectionViewCell.reuseIdentifier)
        collectionView.register(UINib(nibName: WideStepCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: WideStepCollectionViewCell.reuseIdentifier)
        groupTexts()
        addNavButton()
        addBackRightButton()

        navigationController?.navigationBar.barTintColor = .darkGray
        
        guard problemItem != nil else{ return }
        collectionView.delegate = self
        collectionView.dataSource = self
        headerImage = ImageRecord(name: "", url: URL(string: problemItem?.image ?? "") ?? nil)
        demoImage = ImageRecord(name: "", url: URL(string: (problemItem?.demo ?? "")) ?? nil)
        guard let problem = problemItem, let products = problem.products, let steps = problem.steps else { return }
        productImages.append(contentsOf: products.map{ ImageRecord(name: "", url: URL(string: $0.image ?? "") ?? nil) })
        stepImages.append(contentsOf: steps.map{ ImageRecord(name: "", url: URL(string: $0.image ?? "") ?? nil) })
        
    }
    
    func showProductDetails(product: Product) {
        if let vc = storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Product) as? ProductViewController {
            vc.productId = product.id ?? 0
            vc.product = product
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    override func reloadIndexPaths(indexPaths: [IndexPath]) {
        collectionView.reloadItems(at: indexPaths)
    }
    
    func groupTexts() {
        addIndicator()
        guard let steps = problemItem?.steps else { return }
        let chunkedTexts = steps.map { $0.caption }.chunk(2)
        chunkedTexts.forEach { chunk in
            if chunk.count % 2 != 0 {
                let height1 = chunk.first??.height(withConstrainedWidth: collectionView.frame.width - 30.0, font: Constants.textFont) ?? (collectionView.frame.width * 1.65) - 30.0
                let height2 = chunk.last??.height(withConstrainedWidth: collectionView.frame.width - 30.0, font: Constants.textFont) ?? (collectionView.frame.width * 1.65) - 30.0
                self.textHeights.append(height1 > height2 ? height1: height2)
            } else {
                let height1 = chunk.first??.height(withConstrainedWidth: Constants.stepRestrictedWidth, font: Constants.textFont) ?? (collectionView.frame.width * 1.65) - 30.0
                let height2 = chunk.last??.height(withConstrainedWidth: Constants.stepRestrictedWidth, font: Constants.textFont) ?? (collectionView.frame.width * 1.65) - 30.0
                self.textHeights.append(height1 > height2 ? height1: height2)
            }
        }
        removeIndicator()
    }
    
}

extension SuggestionStepsViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {


    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if indexPath.section == 0 {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StepsHeaderCollectionViewCell.reuseIdentifier, for: indexPath) as! StepsHeaderCollectionViewCell
            
            cell.title.text = self.problemItem?.title ?? ""
            cell.image.image = headerImage.image
            if headerImage.state == .New {
                startDownloadForRecord(photoDetails: headerImage, indexPath: indexPath)
            }
            return cell
        }
        
        if indexPath.section == 1 {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ApplicationHeaderCollectionViewCell.reuseIdentifier, for: indexPath) as! ApplicationHeaderCollectionViewCell
            return cell
        }
        
        if indexPath.section == 2 {
            if indexPath.row == 0 {
                let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProductHeaderCollectionViewCell.reuseIdentifier, for: indexPath) as! ProductHeaderCollectionViewCell
                cell.backgroundColor = UIColor.darkGray
                return cell
            }
            let product = self.problemItem!.products![indexPath.row - 1]
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: SuggestionProductCollectionViewCell.reuseIdentifier, for: indexPath) as! SuggestionProductCollectionViewCell
            cell.title.text = product.title!
            cell.backgroundColor = UIColor.darkGray
            let productImage = productImages[indexPath.row - 1]
            cell.image.image = productImage.image
            if productImage.state == .New {
                startDownloadForRecord(photoDetails: productImage, indexPath: indexPath)
            }
            
            return cell
        }
        
        if indexPath.section == 3 {
            if indexPath.row == 0 {
                let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StepsTitleCollectionViewCell.reuseIdentifier, for: indexPath) as! StepsTitleCollectionViewCell
                return cell
            }
            if let steps = problemItem?.steps, indexPath.row == steps.count, steps.count % 2 != 0 {
                let cell = collectionView.dequeueReusableCell(withReuseIdentifier: WideStepCollectionViewCell.reuseIdentifier, for: indexPath) as! WideStepCollectionViewCell
                guard let item = problemItem?.steps?[indexPath.row - 1] else { return UICollectionViewCell() }
                if let caption = item.caption {
                    cell.stepDescription.attributedText = caption.htmlAttributedString
                }
                let stepImage = stepImages[indexPath.row - 1]
                cell.image.image = stepImage.image
                if stepImage.state == .New {
                    startDownloadForRecord(photoDetails: stepImage, indexPath: indexPath)
                }
                return cell
            } else {
                let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StepCollectionViewCell.reuseIdentifier, for: indexPath) as! StepCollectionViewCell
                guard let item = problemItem?.steps?[indexPath.row - 1] else { return UICollectionViewCell() }
                if let caption = item.caption {
                    cell.stepDescription.attributedText = caption.htmlAttributedString
                }
                let stepImage = stepImages[indexPath.row - 1]
                cell.image.image = stepImage.image
                if stepImage.state == .New {
                    startDownloadForRecord(photoDetails: stepImage, indexPath: indexPath)
                }
                return cell
            }
        }
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: SuggestionExampleCollectionViewCell.reuseIdentifier, for: indexPath) as! SuggestionExampleCollectionViewCell
        cell.image.image = demoImage.image
        if demoImage.state == .New {
            startDownloadForRecord(photoDetails: demoImage, indexPath: indexPath)
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.section == 2 && indexPath.row > 0 {
            guard let products = problemItem?.products else { return }
            guard let productId = products[indexPath.row - 1].id else { return }
            addIndicator()
            presenter.getProductInfo(id: productId, completion: {
                success, response in
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    guard success, let product = response.first as? Product else {
                        strongSelf.removeIndicator()
                        return
                    }
                    strongSelf.removeIndicator()
                    strongSelf.showProductDetails(product: product)
                }
            })
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if indexPath.section == 0 {
            return CGSize(width: collectionView.frame.width, height: collectionView.frame.width + 75.0)
        }
        if indexPath.section == 1 {
            return CGSize(width: collectionView.frame.width, height: Utils.transformHeight(height: 100.0))
        }
        if indexPath.section == 2 {
            return CGSize(width: collectionView.frame.width, height: Utils.transformHeight(height: 100.0))
        }
        
        if indexPath.section == 3 {
            if indexPath.row == 0 {
                return CGSize(width: collectionView.frame.width - 10.0, height: Utils.transformHeight(height: 60.0))
            }
            guard let steps = problemItem?.steps else { return CGSize(width: collectionView.frame.width / 2 - 15.0 , height: Utils.transformHeight(height: 300.0))  }
            
            guard !textHeights.isEmpty && !(textHeights.count < (indexPath.row - 1) / 2) else {
                if indexPath.row == steps.count, steps.count % 2 != 0 {
                    return CGSize(width: collectionView.frame.width - 20.0, height: Constants.defaultCellHeight * 2)
                }
                return CGSize(width: collectionView.frame.width / 2 - 15.0 , height: Constants.defaultCellHeight)
            }
            if indexPath.row == steps.count, steps.count % 2 != 0 {
                return CGSize(width: collectionView.frame.width - 20.0, height: textHeights[Int((indexPath.row - 1) / 2)] + (collectionView.frame.width) + 25.0)
            }
            return CGSize(width: collectionView.frame.width / 2 - 15.0 , height: textHeights[Int((indexPath.row - 1) / 2)] + (collectionView.frame.width / 2) + 25.0)
        }
        return CGSize(width: collectionView.frame.width , height: collectionView.frame.width)
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 5
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        if section == 3 {
            return UIEdgeInsets.init(top: 0.0, left: 10.0, bottom: 0.0, right: 10.0)
        }
        return UIEdgeInsets.init(top: 0.0, left: 0.0, bottom: 0.0, right: 0.0)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if section == 0 {
            return (problemItem?.image == nil || problemItem?.image == "") ? 0 : 1
        }
        if section == 2 {
            guard let products = problemItem?.products else { return 0 }
            return products.count + 1
        }
        if section == 3 {
            guard let steps = problemItem?.steps else { return 0}
            return steps.count + 1
        }
        return (problemItem?.demo == nil || problemItem?.demo == "") ? 0 : 1
    }

}
