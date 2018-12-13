//
//  Array.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 1/20/18.
//  Copyright © 2018 Sika. All rights reserved.
//

import Foundation

extension Array {
    func chunk(_ chunkSize: Int) -> [[Element]] {
        return stride(from: 0, to: self.count, by: chunkSize).map({ (startIndex) -> [Element] in
            let endIndex = (startIndex.advanced(by: chunkSize) > self.count) ? self.count-startIndex : chunkSize
            return Array(self[startIndex..<startIndex.advanced(by: endIndex)])
        })
    }
}
