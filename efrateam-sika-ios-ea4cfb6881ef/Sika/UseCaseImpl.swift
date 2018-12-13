//
//  UseCaseImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 7/6/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class UseCaseImpl {
    let repository: Repository
    let service: Service
    
    required init(repository: Repository, service: Service) {
        self.repository = repository
        self.service = service
    }
}
