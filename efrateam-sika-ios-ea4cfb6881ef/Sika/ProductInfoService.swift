//
//  ProductInfoService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol ProductInfoService: Service {
    func getProductInfo(id: Int, completion: @escaping (ServiceResponse) -> Void)
}
