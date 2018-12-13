//
//  FindStoreViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import CarbonKit

class FindStoreViewController: BaseViewController, CarbonTabSwipeNavigationDelegate {
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.title = "Buscar tienda..."
        self.navigationController?.navigationBar.tintColor = UIColor.white
        
        let items = ["Mapa", "Estado"]
        
        let carbonTabSwipeNavigation = CarbonTabSwipeNavigation(items: items, delegate: self)
        carbonTabSwipeNavigation.insert(intoRootViewController: self)
        carbonTabSwipeNavigation.carbonSegmentedControl?.indicator.backgroundColor = UIColor.mainColor
        carbonTabSwipeNavigation.setIndicatorHeight(5.0)
        carbonTabSwipeNavigation.setTabBarHeight(50.0)
        carbonTabSwipeNavigation.setSelectedColor(UIColor.white)
        carbonTabSwipeNavigation.setNormalColor(UIColor.white)
        carbonTabSwipeNavigation.toolbar.barTintColor = UIColor.darkGray
        carbonTabSwipeNavigation.setIndicatorColor(UIColor.mainColor)
        
        carbonTabSwipeNavigation.carbonSegmentedControl?.setWidth(self.view.frame.size.width / 2, forSegmentAt: 0)
        carbonTabSwipeNavigation.carbonSegmentedControl?.setWidth(self.view.frame.size.width / 2, forSegmentAt: 1)
        self.addNavButton()
    }
    
    
    func carbonTabSwipeNavigation(_ carbonTabSwipeNavigation: CarbonTabSwipeNavigation, viewControllerAt index: UInt) -> UIViewController {
        switch index {
        case 1:
            return self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.States) as! StatesViewController
        default:
            return self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Map) as! MapViewController
        }
    }
    
}
