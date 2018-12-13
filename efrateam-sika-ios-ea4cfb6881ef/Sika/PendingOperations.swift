//
//  PendingOperations.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class PendingOperations {
    
    lazy var downloadsInProgress = [IndexPath : Operation]()
    lazy var downloadsQueued = [Int : Operation]()
    lazy var downloadQueue: OperationQueue = {
        var queue = OperationQueue()
        queue.name = "Download queue"
        queue.maxConcurrentOperationCount = 1
        return queue
    }()
    
}
