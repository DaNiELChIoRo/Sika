//
//  SuggestionViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

class SuggestionViewController: BaseCollectionViewController {

    @IBOutlet weak var tableView: UITableView!
    
    let segueToSteps = "segueToSteps"
    let cellFont = UIFont(name: Font.FontName.OpenSansBold, size: 18.0)!
    var headerImage: ImageRecord!
    var suggestion: Suggestion?
    var problemItem: ProblemItem?
    var images: [ImageRecord] = []
    var sectionName: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationController?.navigationBar.barTintColor = .darkGray
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        navigationController?.navigationBar.barTintColor = .mainColor
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? SuggestionStepsViewController {
            destination.suggestion = suggestion
            destination.problemItem = problemItem
        }
    }
    
    override func reloadIndexPaths(indexPaths: [IndexPath]) {
        tableView.reloadRows(at: indexPaths, with: .none )
    }

}
extension SuggestionViewController : UITableViewDelegate, UITableViewDataSource {
    
    func configureUI() {
        title = "Problemas comunes"
        headerImage = ImageRecord(name: "", url: URL(string: suggestion?.image ?? "") ?? nil)
        addNavButton()
        addBackRightButton()
        configureTableView()
        guard let items = suggestion?.items else { return }
        items.forEach { self.images.append(ImageRecord(name: "", url: URL(string: $0.image ?? "") ?? nil)) }
    }
    
    func configureTableView() {
        tableView.register( UINib(nibName: SuggestionHeaderTableViewCell.reuseIdentifier, bundle: nil), forCellReuseIdentifier: SuggestionHeaderTableViewCell.reuseIdentifier)
        tableView.register( UINib(nibName: SuggestionTableViewCell.reuseIdentifier, bundle: nil), forCellReuseIdentifier: SuggestionTableViewCell.reuseIdentifier)
        tableView.separatorStyle = .none
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard let items = suggestion?.items else { return }
        problemItem = items[indexPath.row - 1]
        performSegue(withIdentifier: segueToSteps, sender: self)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        guard let items = suggestion?.items else { return 0 }
        return items.count + 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return Utils.getScreenSize().height * 0.7
        }
        return 90.0
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: SuggestionHeaderTableViewCell.reuseIdentifier) as! SuggestionHeaderTableViewCell
            cell.title.text = suggestion?.title ?? ""
            cell.section.text = sectionName ?? ""
            cell.logo.image = headerImage.image
            if headerImage.state == .New {
                startDownloadForRecord(photoDetails: headerImage, indexPath: indexPath)
            }
            cell.backgroundColor = UIColor.init(white: 0.4, alpha: 1.0)
            return cell
        }
        guard let items = suggestion?.items else { return UITableViewCell() }
        let cell = tableView.dequeueReusableCell(withIdentifier: SuggestionTableViewCell.reuseIdentifier) as! SuggestionTableViewCell
        cell.selectionStyle = .none
        let item = items[indexPath.row - 1]
        cell.title.text = item.title ?? ""
        cell.icon.image = images[indexPath.row - 1].image
        if images[indexPath.row - 1].state == .New {
            startDownloadForRecord(photoDetails: images[indexPath.row - 1], indexPath: indexPath)
        }
        return cell
    }

}
