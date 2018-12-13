//
//  StateDetailsService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol StateDetailsService: Service {
    func getStateDetails(stateId: Int, completion: @escaping (ServiceResponse) -> Void)
}
