//
//  UIViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 26/05/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController{

    struct Constants {
        struct Button {
            static let Cancel = "CANCEL".localized()
            static let Retry = "RETRY".localized()
        }
        
        struct Title {
            static let Oops = "OOPS"
            static let Error = "ERROR"
            static let Warning = "WARNING"
        }
    }
    
    enum AlertType {
        case regular
        case retry
    }
    
    /**
     * 	@brief Present different UIAlertController according to the AlertType.
     * 	@param type a AlertType containing the alert type to show.
     * 	@param message a string containing the message to show in the Alert.
     *  @param retryCompletion a closure containing logic for retry action.
     */
    func presentAlert (type:AlertType, message: String, retryCompletion: (() -> Void)?) {
        switch type {
        case .regular:
            self.presentAlert(title:Constants.Title.Oops, message: message)
        case .retry:
            self.presentAlert(title: Constants.Title.Error, message: message, retryCompletion: retryCompletion)
        }
    }
    
    /**
     * 	@brief Present UIAlertController.
     * 	@param title a string containing the title for the Alert.
     * 	@param message a string containing the message to show in the Alert.
     */
    func presentAlert (title: String, message: String) {
        let alert = createAlert(title: title, message: message)
        let okAction = createAlertAction(title: Constants.Button.Cancel, style: UIAlertActionStyle.cancel, handler: nil)
        
        alert.addAction(okAction)
        self.present(alert, animated: true, completion: nil)
    }
    
    /**
     * 	@brief Present UIAlertController.
     * 	@param title a string containing the title for the Alert.
     * 	@param message a string containing the message to show in the Alert.
     *  @param retryCompletion a closure containing logic for retry action.
     */
    func presentAlert (title: String, message: String, retryCompletion: (() -> Void)?) {
        let alert = createAlert(title: title, message: message)
        let okAction = createAlertAction(title: Constants.Button.Cancel, style: UIAlertActionStyle.cancel, handler: nil)
        let retryAction = createAlertAction(title: Constants.Button.Retry, style: UIAlertActionStyle.default, handler: retryCompletion)
        
        alert.addAction(okAction)
        alert.addAction(retryAction)
        self.present(alert, animated: true, completion: nil)
    }
    
    /**
     * 	@brief Present UIAlertController without title.
     * 	@param message a string containing the message to show in the Alert.
     */
    func presentEmptyAlert (message: String) {
        let alert = createAlert(title: nil, message: message)
        
        self.present(alert, animated: true, completion: nil)
    }
    
    static func embedInAppNavigationViewController() -> UINavigationController {
        let nav = UINavigationController(rootViewController: self.init())
        nav.navigationItem.leftBarButtonItem = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Menu), style: .plain, target: self, action: #selector(openMenu))
        return nav
    }
    
    func addNavButton() {
        self.navigationItem.leftBarButtonItem = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Menu), style: .plain, target: self, action: #selector(openMenu))
    }
    
    func addBackRightButton() {
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: AppConstants.Icons.Back), style: .plain, target: self, action: #selector(back))    
    }

    func back() {
        self.navigationController?.popViewController(animated: true)
    }
    
    func openMenu() {
        self.slideMenuController()?.openLeft()
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
    
}

private extension UIViewController {
    
    func createAlert(title: String?, message: String) -> UIAlertController {
        return UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
    }
    
    func createAlertAction(title: String, style: UIAlertActionStyle, handler: (() -> Void)?) -> UIAlertAction {
        if let handler = handler {
            return UIAlertAction(title: title, style: style) {
                (result : UIAlertAction) -> Void in
                handler()
            }
        } else {
            return UIAlertAction(title: title, style: style, handler: nil)
        }
    }
}
