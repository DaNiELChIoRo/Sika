//
//  StatesService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol StatesService {
    func getStates(countryId: Int, completion: @escaping (ServiceResponse) -> Void)
}
