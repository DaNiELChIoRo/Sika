//
//  SliderService.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/29/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

protocol SliderService: Service {
    func getSliders(completion: @escaping (ServiceResponse) -> Void)
}
