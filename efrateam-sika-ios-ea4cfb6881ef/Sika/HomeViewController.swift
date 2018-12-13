//
//  HomeViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import NVActivityIndicatorView

class HomeViewController: BaseCollectionViewController {

    let segueToSuggestion = "segueToSuggestion"
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    struct Constants {
        static let headerHeight: CGFloat = Utils.transformHeight(height: 60.0)
    }
    
    var items:[SuggestionCategory] = []
    var pages:[SliderItem] = []
    var images: [[ImageRecord]] = []
    var presenter: TaxonomiesPresenter!
    
    var suggestion: Suggestion?
    var selectedSection: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: .homeViewController) as! TaxonomiesPresenter
        configureNav()
        configureUI()
    }
   
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? SuggestionViewController {
            destination.suggestion = suggestion
            destination.sectionName = selectedSection
        }
    }
    
    override func reloadIndexPaths(indexPaths: [IndexPath]) {
        collectionView.reloadItems(at: indexPaths)
    }
    
}

extension HomeViewController {

    func configureNav() {
        addNavButton()
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Search), style: .plain, target: self, action: #selector(searchButtonPressed))
        let logo = UIImage(named: AppConstants.Icons.SikaHeader)
        let imageView = UIImageView(image: logo)
        navigationItem.titleView = imageView
    }
    
    func configureUI() {
        configureCollectionView()
        getDataSources()
    }
    
    func configureCollectionView() {
        collectionView.backgroundColor = UIColor.mainColor
        let layout = UICollectionViewFlowLayout()
        layout.minimumLineSpacing = 0.0
        layout.minimumInteritemSpacing = 0.0
        collectionView.collectionViewLayout = layout
        collectionView.register(UINib(nibName: TaxonomySeparatorCollectionViewCell.reuseIdentifier, bundle: nil), forSupplementaryViewOfKind: UICollectionElementKindSectionHeader, withReuseIdentifier: TaxonomySeparatorCollectionViewCell.reuseIdentifier)
        collectionView.register(UINib(nibName: HomeCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: HomeCollectionViewCell.reuseIdentifier)
        collectionView.register(UINib(nibName: HomePagerCollectionViewCell.reuseIdentifier, bundle: nil), forCellWithReuseIdentifier: HomePagerCollectionViewCell.reuseIdentifier)

        collectionView.dataSource = self
        collectionView.delegate = self
    }
    
    func getDataSources() {
        addIndicator()
        presenter.getSuggestions(completion: {
            success, response in
            guard let suggestions = response as? [SuggestionCategory] else { return }
            self.items.append(contentsOf: suggestions)
            for suggestion in suggestions {
                guard let items = suggestion.items else { continue }
                var sectionImages: [ImageRecord] = []
                items.forEach {
                    if let stringUrl = $0.image, let url = URL(string: stringUrl) {
                        sectionImages.append(ImageRecord(name: "", url: url))
                    } else {
                        sectionImages.append(ImageRecord(name: "", url: nil))
                    }
                }
                self.images.append(sectionImages)
            }
            DispatchQueue.main.async { [weak self] in
                if let strongSelf = self {
                    strongSelf.collectionView.reloadData()
                    strongSelf.removeIndicator()
                    strongSelf.getSlides()
                }
            }
        })
    }

    func searchButtonPressed() {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Search) as! SearchViewController
        vc.rootControllerCase = .home
        navigationController?.pushViewController(vc, animated: true)
    }
    
    func getSlides() {
        presenter.getSliders(completion: {
            success, response in
            if let sliders = response as? [SliderItem] {
                self.pages.append(contentsOf: sliders)
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.collectionView.reloadItems(at: [IndexPath(row: 0, section: 0)])
                }
            }
        })
    }
    
}

extension HomeViewController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if indexPath.section == 0 {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: HomePagerCollectionViewCell.reuseIdentifier, for: indexPath) as! HomePagerCollectionViewCell
            cell.setup(pages: pages)
            cell.tapCallback = {
                self.slideMenuController()?.changeMainViewController(UINavigationController(rootViewController: (self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Promotions))!), close: true)

            }
            return cell
        }
        let cell = self.collectionView.dequeueReusableCell(withReuseIdentifier: HomeCollectionViewCell.reuseIdentifier, for: indexPath) as! HomeCollectionViewCell
        let item = items[indexPath.section - 1].items?[indexPath.row]
        let suggestionImage = images[indexPath.section - 1][indexPath.row]
        cell.image.image = suggestionImage.image
        if suggestionImage.state == .New {
            startDownloadForRecord(photoDetails: suggestionImage, indexPath: indexPath)
        }
        cell.categoryLabel.textColor = .white
        cell.categoryLabel.text = item?.title ?? ""
        return cell
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return items.count + 1
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard indexPath.section > 0 else { return }
        selectedSection = items[indexPath.section - 1].title
        suggestion = items[indexPath.section - 1].items?[indexPath.row]
        performSegue(withIdentifier: self.segueToSuggestion, sender: self)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if section == 0 {
            return 1
        }
        guard let items = items[section - 1].items else {
            return 0
        }
        return items.count
    }

    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        switch kind {
        case UICollectionElementKindSectionHeader:
            let reusableview = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionElementKindSectionHeader, withReuseIdentifier: TaxonomySeparatorCollectionViewCell.reuseIdentifier, for: indexPath) as! TaxonomySeparatorCollectionViewCell
            if let title = items[indexPath.section - 1].title {
                reusableview.titleLabel.text = title
            }
            return reusableview
        default:  fatalError("Unexpected element kind")
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        if section == 0 {
            return CGSize.zero
        }
        return CGSize(width: Utils.getScreenSize().width, height: Constants.headerHeight)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if indexPath.section == 0 {
            return CGSize(width: Utils.getScreenSize().width, height: Utils.transformHeight(height: 200.0))
        }
        return CGSize(width: Utils.getScreenSize().width/2, height: Utils.transformHeight(height: 200.0))
    }

}
