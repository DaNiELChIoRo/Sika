//
//  BaseViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 26/05/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import NVActivityIndicatorView

class BaseViewController: UIViewController {
    
    var activityIndicatorView: NVActivityIndicatorView?
    //let titleFont: UIFont = UIFont(name: Font.FontName.OpenSansLightItalic, size: 25.0)!

    func addIndicator() {
        if activityIndicatorView == nil{
            activityIndicatorView = NVActivityIndicatorView(frame: self.view.frame, type: .ballClipRotate , color: UIColor.white)
            activityIndicatorView?.frame.size = CGSize(width: 100.0, height: 100.0)
            activityIndicatorView?.center = self.view.center
        }
        activityIndicatorView?.startAnimating()
        view.addSubview(activityIndicatorView!)
    }
    
    func removeIndicator() {
        guard let activityIndicatorView = activityIndicatorView else { return }
        activityIndicatorView.stopAnimating()
        if activityIndicatorView.superview != nil {
            activityIndicatorView.removeFromSuperview()
        }
    }
    
    func addSimpleBackButton(_ mTarget: Any?) {
        navigationItem.backBarButtonItem = UIBarButtonItem(title: " ", style: .plain, target: mTarget, action: nil)
    }
    
    func addHomeButton(_ mTarget: Any?) {
        let image = UIImage(named: AppConstants.Icons.HomeIconWhite)?.withRenderingMode(.alwaysTemplate)
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: image, style: .plain, target: self, action: #selector(homeTapped(_:)))
    }
    
    @objc func homeTapped(_ sender: Any) {
        let menuStoryboard = UIStoryboard(name: Storyboards.Menu, bundle: nil)
        slideMenuController()?.changeMainViewController(menuStoryboard.instantiateInitialViewController()!, close: true)

    }
    
    struct Constants {
        struct Message {
            static let Error = "REQUEST_ERROR"
            static let NotConnectedToInternet = "NOT_CONNECTED_TO_INTERNET"
        }
    }
    
    func handleError() {
        DispatchQueue.main.async { [weak self] in
            if let strongSelf = self {
                strongSelf.presentAlert(type: AlertType.retry, message: strongSelf.errorMessage()) {
                    
                }
            }
        }
    }
    
    func handleNotConnectedToInternet() {
        DispatchQueue.main.async { [weak self] in
            if let strongSelf = self {
                strongSelf.presentAlert(type: AlertType.retry, message: Constants.Message.NotConnectedToInternet) {
                    
                }
            }
        }
    }
    
    func loadServices() {
        // Pure virtual
    }
    
    func errorMessage() -> String {
        return Constants.Message.Error
    }

}
