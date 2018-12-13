//
//  Repository.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 26/05/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

enum SaveStatus {
    case success
    case failure
}

protocol Repository {
    func cancelAllRequest()
}
