//
//  GlossaryViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

class GlossaryViewController: BaseViewController {

    @IBOutlet weak var tableView: UITableView!
    var glossary: [GlossaryItem] = []
    var presenter: TaxonomiesPresenter!
    var descriptionFont: UIFont!

    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: .homeViewController) as! TaxonomiesPresenter
        descriptionFont = UIFont(name: Font.FontName.OpenSansRegular, size: 15.0)!
        configureUI()
    }

}

extension GlossaryViewController: UITableViewDelegate, UITableViewDataSource {

    func configureUI() {
        addNavButton()
        addHomeButton(nil)
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: AppConstants.Cells.Glossary, bundle: nil), forCellReuseIdentifier: AppConstants.Cells.Glossary)
        tableView.delegate = self
        tableView.dataSource = self
        
        addIndicator()
        presenter.getGlossary() {
            success, response in
            if let glossary = response as? [GlossaryItem] {
                self.glossary = glossary
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.tableView.reloadData()
                    strongSelf.removeIndicator()
                }
            } else {
                DispatchQueue.main.async { [weak self] in
                    guard let strongSelf = self else { return }
                    strongSelf.removeIndicator()
                }
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AppConstants.Cells.Glossary) as! GlossaryCell
        cell.backgroundColor = .clear
        let item = glossary[indexPath.row]
        if let title = item.title {
            cell.titleLabel.text = title
        }
        if let description = item.description {
            cell.subtitleLabel.text = description
        }
        return cell
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return glossary.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        var height: CGFloat = 50.0
        if let descriptionHeight = glossary[indexPath.row].description?.height(withConstrainedWidth: tableView.frame.size.width - 20.0, font: descriptionFont) {
            height += descriptionHeight
        }
        return height
    }
    
}
