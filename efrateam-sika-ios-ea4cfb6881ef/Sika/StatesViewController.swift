//
//  StatesViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import UIKit

enum StatesDisplayStatus {
    case states
    case districts
}

class StatesViewController: BaseViewController {

    @IBOutlet weak var tableView: UITableView!
    var presenter: FindStoresPresenter!
    
    var states: [State] = []
    var districts: [String] = []
    
    var selectedDistrict: String = ""
    
    var status: StatesDisplayStatus = .states

    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = PresenterLocator.getPresenter(ofType: PresenterType.findStoreViewController) as! FindStoresPresenter
        configureUI()
        getStates()
    }
    
    func getDistricts(with state: State) {
        addIndicator()
        presenter.getDistricts(stateId: state.id ?? -1) {
            success, districts in
            DispatchQueue.main.async { [weak self] in
                guard let strongSelf = self else { return }
                strongSelf.districts.removeAll()
                strongSelf.districts.append(contentsOf: districts)
                strongSelf.status = .districts
                strongSelf.tableView.reloadData()
                strongSelf.removeIndicator()
            }
        }
    }
    
    func getStates() {
        addIndicator()
        presenter.getStates(countryId: AppConstants.Countries.Default) {
            success, response in
            DispatchQueue.main.async { [weak self] in
                guard let strongSelf = self else { return }
                if let states = response as? [State] {
                    strongSelf.states.removeAll()
                    strongSelf.states.append(contentsOf: states)
                    strongSelf.tableView.reloadData()
                    strongSelf.removeIndicator()
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == SeguesId.nearStoresSegue, let vc = segue.destination as? NearStoreViewController {
            vc.district = selectedDistrict
        }
    }
}

extension StatesViewController: UITableViewDataSource, UITableViewDelegate {
    
    func configureUI() {
        configureTableView()
    }
    
    func configureTableView() {
        tableView.register(UINib(nibName: AppConstants.Cells.State, bundle: nil), forCellReuseIdentifier: AppConstants.Cells.State)
        tableView.separatorStyle = .none
        tableView.alwaysBounceVertical = false
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = .clear
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch status {
        case .districts:
            return districts.count
        case .states:
            return states.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AppConstants.Cells.State) as! StateTableViewCell
        switch status {
        case .districts:
            cell.titleLabel.text = districts[indexPath.row]
        case .states:
            cell.titleLabel.text = states[indexPath.row].name ?? ""
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch status {
        case .districts:
            selectedDistrict = districts[indexPath.row]
            performSegue(withIdentifier: SeguesId.nearStoresSegue, sender: self)
        case .states:
            getDistricts(with: states[indexPath.row])
        }
    }
    
}
