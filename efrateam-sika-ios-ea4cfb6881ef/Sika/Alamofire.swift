//
//  Alamofire.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 6/19/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import Alamofire

extension Alamofire.SessionManager{
    //Access it with Alamofire.SessionManager.default.request
    open func request(
        _ endPoint: String,
        method: HTTPMethod = .get,
        cachingEnabled: Bool) -> DataRequest{
        do {
            var urlRequest = try URLRequest(url: endPoint, method: method)
            if !cachingEnabled{
                urlRequest.cachePolicy = .reloadIgnoringCacheData
            }
            let encoding: ParameterEncoding = URLEncoding.default
            let encodedURLRequest = try encoding.encode(urlRequest, with: nil)
            return request(encodedURLRequest)
        } catch {
            //TO DO: Change the url to an error URL
            return request(URLRequest(url: URL(string: "http://example.com/wrong_request")!))
        }
    }
}
