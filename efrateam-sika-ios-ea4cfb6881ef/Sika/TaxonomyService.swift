//
//  TaxonomyService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol TaxonomyService {
    func getTaxonomies(completion: @escaping (ServiceResponse) -> Void)
}
