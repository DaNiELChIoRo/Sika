//
//  BaseService.swift
//  excalibur
//
//  Created by Nicolas Porpiglia on 3/11/17.
//  Copyright © 2017 rccl. All rights reserved.
//

import UIKit
import Alamofire

// Override this protocol in all the Services that need it
protocol ProductKey {
    var kProductsArrayKey: String { get }
}

enum ResponseStatusCodes: Int {
    case successCode = 200
    case successCreated = 201
    case unauthorizedCode = 401
    case unprocessableEntity = 422
}

class BaseService: Service, ProductKey {
    
    var dataRequestArray: [DataRequest] = []
    var sessionManagers: [String : Alamofire.SessionManager] = [:]
    
    public var kProductsArrayKey: String {
        return ""
    }
    
    //enableCache Var. is just for testing purposes. Set it False to disable Caching
    static var enableCache = true
    
    var endpoint: String?
    
    let productsArray: String = "product"
    
    func callEndpoint(endPoint: String, method: Alamofire.HTTPMethod = .get, page: Int = 0, headers: [String:String]? = [:], params: [String : Any]? = [:], completion:@escaping (ServiceResponse) -> Void) {
        
        let dataRequest: DataRequest?
        
        switch method {
        case .post:
            dataRequest = request(endPoint, method: method, parameters: params, encoding: JSONEncoding.default, headers: headers).responseString { (response) in
                self.serializeResponse(response: response, page: page, completion: completion)
                self.sessionManagers.removeValue(forKey: endPoint)
            }
        case .put:
            dataRequest = request(endPoint, method: method, parameters: params, encoding: JSONEncoding.default, headers: headers).responseString { (response) in
                self.serializeResponse(response: response, page: page, completion: completion)
                self.sessionManagers.removeValue(forKey: endPoint)
            }
        default:
            dataRequest = request(endPoint, method: method, parameters: params, headers: headers).responseString { (response) in
                self.serializeResponse(response: response, page: page, completion: completion)
                self.sessionManagers.removeValue(forKey: endPoint)
            }
        }
        
        if let dataRequest = dataRequest {
            self.dataRequestArray.append(dataRequest)
        }
    }
    
    func serializePostResponse(response: Alamofire.DataResponse<String>, page: Int, completion:@escaping (ServiceResponse) -> Void) {
        
        DispatchQueue.global(qos: .background).async { [weak self] in
            
            guard let strongSelf = self else { return }
            
            var json: Any?
            
            guard let urlResponse = response.response else {
                if let error = response.result.error as NSError?, error.code == NSURLErrorNotConnectedToInternet {
                    strongSelf.notConnectedToInternet(completion: completion)
                } else {
                    strongSelf.failure(completion: completion)
                }
                return
            }
            
            if let data = response.result.value?.data(using: String.Encoding.utf8) {
                do {
                    if urlResponse.statusCode == ResponseStatusCodes.successCode.rawValue || urlResponse.statusCode == ResponseStatusCodes.successCreated.rawValue {
                        json = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? String
                    } else {
                        json = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String:AnyObject]
                    }
                } catch {
                    strongSelf.failure(completion: completion)
                    return
                }
            }
            
            strongSelf.processResponse(json: json, urlResponse: urlResponse, page: page, completion: completion)
        }
    }
    
    func serializeResponse(response: Alamofire.DataResponse<String>, page: Int, completion: @escaping (ServiceResponse) -> Void) {
        
        DispatchQueue.global(qos: .background).async { [weak self] in
            
            guard let strongSelf = self else { return }
            
            var json: Any?
            
            guard let urlResponse = response.response else {
                if let error = response.result.error as NSError?, error.code == NSURLErrorNotConnectedToInternet {
                    strongSelf.notConnectedToInternet(completion: completion)
                } else {
                    strongSelf.failure(completion: completion)
                }
                return
            }
            
            if let data = response.result.value?.data(using: String.Encoding.utf8) {
                do {
                    json = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String:AnyObject]
                } catch {
                    if urlResponse.statusCode == ResponseStatusCodes.successCode.rawValue {
                        strongSelf.success(result: [], headers: urlResponse.allHeaderFields, completion: completion)
                    } else {
                        strongSelf.failure(completion: completion)
                    }
                    return
                }
            }
            
            strongSelf.processResponse(json: json, urlResponse: urlResponse, page: page, completion: completion)
        }
    }
    
    func processResponse(json: Any?, urlResponse: HTTPURLResponse, page: Int, completion: @escaping (ServiceResponse) -> Void) {
        
        guard let jsonResponse = json else {
            self.failure(completion: completion)
            return
        }
                
        switch urlResponse.statusCode {
            
        case ResponseStatusCodes.successCode.rawValue, ResponseStatusCodes.successCreated.rawValue:
            if let response = self.parse(response: jsonResponse as AnyObject, page: page) {
                self.success(result: response, headers: urlResponse.allHeaderFields, completion: completion)
            } else {
                self.failure(completion: completion)
            }
        case ResponseStatusCodes.unauthorizedCode.rawValue:
            if let response = self.parseUnauthorized(response: jsonResponse as AnyObject, page: page) {
                self.success(result: response, headers: urlResponse.allHeaderFields, completion: completion)
            } else {
                self.failure(completion: completion)
            }
        case ResponseStatusCodes.unprocessableEntity.rawValue:
            if let response = self.parseUnprocessableEntity(response: jsonResponse as AnyObject, page: page) {
                self.success(result: response, headers: urlResponse.allHeaderFields, completion: completion)
            } else {
                self.failure(completion: completion)
            }
        case NSURLErrorNotConnectedToInternet:
            self.notConnectedToInternet(completion: completion)
            
        default:
            self.failure(completion: completion)
        }
    }
    
    func cancelAllRequests () {
        for dataRequest in self.dataRequestArray {
            dataRequest.cancel()
        }
        self.dataRequestArray.removeAll()
    }
    
    /**
     * Parse method
     * Pure virtual, this is intended to be overrided with a custom parsing method
     * @param: {String} completion - Initial block with response
     */
    
    func parse (response: AnyObject, page: Int = 0) -> [AnyObject]? {
        return nil
    }
    
    /**
     * Parse method
     * Pure virtual, this is intended to be overrided with a custom parsing method
     * @param: {String} completion - Initial block with response
     */
    
    func parseUnauthorized (response: AnyObject, page: Int = 0) -> [AnyObject]? {
        return nil
    }
    
    /**
     * Parse method
     * Pure virtual, this is intended to be overrided with a custom parsing method
     * @param: {String} completion - Initial block with response
     */
    
    func parseUnprocessableEntity (response: AnyObject, page: Int = 0) -> [AnyObject]? {
        return nil
    }
    
    /**
     * Not connected method
     * Override as needed, this provides a default implementation for the 'No Connection' result
     * @param: {String} completion - Initial block with response
     */
    func notConnectedToInternet (completion:@escaping (ServiceResponse) -> Void) {
        completion(.notConnectedToInternet)
    }
    
    /**
     * Failure method
     * Override as needed, this provides a default implementation for the failure result
     * @param: {String} completion - Initial block with response
     */
    
    func failure (completion:@escaping (ServiceResponse) -> Void) {
        completion(.failure)
    }
    
    /**
     * Success method
     * Override as needed, this provides a default implementation for the success result
     * @param: {String} result - Parsing result
     * @param: {String} completion - Initial block with response
     */
    
    func success (result: [AnyObject], headers: [AnyHashable: Any], completion:@escaping (ServiceResponse) -> Void) {
        completion(.success(response: result))
    }
}

private extension BaseService {
    
    func request (_ endPoint: String,
                  method: Alamofire.HTTPMethod = .get,
                  parameters: Parameters? = nil,
                  encoding: ParameterEncoding = URLEncoding.default,
                  headers: [String:String]?) -> DataRequest {
        
        if self.sessionManagers[endPoint] == nil {
            self.configureAlamoFireSSLPinning(endPoint: endPoint)
        }
        
        if let session = self.sessionManagers[endPoint] {
            if (type(of: self).enableCache) {
                return session.request(endPoint, method: method, parameters: parameters, encoding: encoding, headers: headers)
            } else {
                return session.request(endPoint, method: method, cachingEnabled: false)
            }
        }
        
        assertionFailure("Somethig went wrong, check the endpoints URL's and this function")
        
        self.sessionManagers[endPoint] = Alamofire.SessionManager()
        return self.sessionManagers[endPoint]!.request(endPoint)
    }
    
    func configureAlamoFireSSLPinning (endPoint: String) {
        
        let serverTrustPolicy = ServerTrustPolicy.pinCertificates(
            certificates: ServerTrustPolicy.certificates(),
            validateCertificateChain: true,
            validateHost: true
        )
        let serverTrustPolicies: [String: ServerTrustPolicy] = [
            endPoint: serverTrustPolicy
        ]
        
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForResource = AppConstants.Services.Timeout
        
        self.sessionManagers[endPoint] = Alamofire.SessionManager(
            configuration: configuration,
            serverTrustPolicyManager: ServerTrustPolicyManager(policies: serverTrustPolicies)
        )
    }
    
}
