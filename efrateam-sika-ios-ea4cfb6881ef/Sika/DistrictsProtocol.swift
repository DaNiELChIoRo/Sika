//
//  DistrictsProtocol.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 11/1/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

typealias DistrictsResponseClosure = (_ succes: Bool, _ response: [String]) -> Void

protocol DistrictsProtocol {
    func execute(stateId: Int, completion: @escaping DistrictsResponseClosure)
}
