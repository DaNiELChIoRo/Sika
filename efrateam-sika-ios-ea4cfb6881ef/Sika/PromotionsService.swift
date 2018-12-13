//
//  PromotionsService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol PromotionsService: Service {
    func getPromotions(completion: @escaping (ServiceResponse) -> Void)
}
