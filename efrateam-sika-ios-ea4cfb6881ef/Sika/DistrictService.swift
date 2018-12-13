//
//  DistrictService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/12/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol DistrictService {
    func getDistricts(stateId: Int, completion: @escaping (ServiceResponse) -> Void)
}
