//
//  MenuViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import SlideMenuControllerSwift

class MenuViewController: SlideMenuController {
    
    override func awakeFromNib() {
        SlideMenuOptions.leftViewWidth = Utils.transformWidth(width: 300.0)
        SlideMenuOptions.contentViewScale = 1.0
        SlideMenuOptions.animationDuration = 0.3
        
        if let controller = self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Home) {
            self.mainViewController = UINavigationController(rootViewController: controller)
        }
        if let controller = self.storyboard?.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Side) {
            self.leftViewController = controller
        }
        super.awakeFromNib()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.backgroundColor = UIColor.clear

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    

}
