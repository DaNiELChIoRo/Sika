//
//  SideViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 07/06/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit
import MessageUI

class SideViewController: BaseViewController {

    @IBOutlet weak var tableView: UITableView!
    var items: [MenuItem] = MenuManager.shared.menuItems
    let findStoreStoryboard = UIStoryboard(name: Storyboards.Stores, bundle: nil)
    let menuStoryboard = UIStoryboard(name: Storyboards.Menu, bundle: nil)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureUI()
    }

}

extension SideViewController: UITableViewDelegate, UITableViewDataSource {
    
    func configureUI() {
        configureTableView()
    }
    
    func configureTableView() {
        tableView.register(UINib(nibName: MenuTableViewCell.reuseIdentifier, bundle: nil), forCellReuseIdentifier: MenuTableViewCell.reuseIdentifier)
        tableView.separatorStyle = .none
        tableView.isScrollEnabled = false
        tableView.delegate = self
        tableView.dataSource = self
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let item = items[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: MenuTableViewCell.reuseIdentifier) as! MenuTableViewCell
        cell.selectionStyle = .none
        if let title = item.title, let icon = item.icon {
            cell.title.text = title
            cell.icon.image = UIImage(named: icon)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 1:
            slideMenuController()?.changeMainViewController(findStoreStoryboard.instantiateInitialViewController()!, close: true)
        case 2:
            slideMenuController()?.changeMainViewController(UINavigationController(rootViewController: menuStoryboard.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Search)), close: true)
        case 3:
            slideMenuController()?.changeMainViewController(UINavigationController(rootViewController: menuStoryboard.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Promotions)), close: true)
        case 4:
            composeEmail()
            slideMenuController()?.closeLeft()
        case 5:
            slideMenuController()?.changeMainViewController(UINavigationController(rootViewController: menuStoryboard.instantiateViewController(withIdentifier: AppConstants.ViewControllers.Glossary)), close: true)
        default:
            slideMenuController()?.changeMainViewController(menuStoryboard.instantiateInitialViewController()!, close: true)
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return Utils.transformHeight(height: 140.0)
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let container = UIView()
        container.backgroundColor = UIColor.mainColor

        let imageView = UIImageView(frame: CGRect(x: 0, y: 10, width: view.frame.size.width, height: Utils.transformHeight(height: 120.0)))
        
        imageView.image = UIImage(named: AppConstants.Icons.Splash)
        container.addSubview(imageView)
        return container
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return Utils.transformHeight(height: 50.0)
    }
    
}

extension SideViewController: MFMailComposeViewControllerDelegate {
    
    func composeEmail() {
        guard MFMailComposeViewController.canSendMail() else {
            print("Mail services are not available")
            if let url = URL(string: "googlegmail:///co?to=\(AppConstants.Mail.destination)"), UIApplication.shared.canOpenURL(url) {
                if #available(iOS 10.0, *) {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                } else {
                    UIApplication.shared.openURL(url)
                }
            } else {
                presentAlert(title: "Alerta", message: "La aplicación de Mail no está configurada")
            }
            return
        }
        let composeVC = MFMailComposeViewController()
        composeVC.mailComposeDelegate = self
        composeVC.setToRecipients([AppConstants.Mail.destination])
        present(composeVC, animated: true, completion: nil)
    }
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
    
}
