//
//  PromotionsServiceImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 9/4/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class PromotionsServiceImpl: UseCaseImpl, PromotionsServiceProtocol {
    
    func execute(completion: @escaping PromotionsResponseClosure) {
        (repository as! ProductsRepository).getPromotions(completion: completion)
    }
    
}
