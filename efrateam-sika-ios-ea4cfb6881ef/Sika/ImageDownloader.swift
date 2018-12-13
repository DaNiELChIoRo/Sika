//
//  ImageDownloader.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

class ImageDownloader: Operation {


    let imageRecord: ImageRecord
    
    init(imageRecord: ImageRecord) {
        self.imageRecord = imageRecord
    }
    
    override func main() {

        if self.isCancelled {
            return
        }
        
        do {
            guard let url = imageRecord.url else { return }
            let imageData = try Data(contentsOf: url)
            if self.isCancelled {
                return
            }
            
            if imageData.count > 0 {
                DispatchQueue.main.async {
                    self.imageRecord.image = UIImage(data:imageData)
                    self.imageRecord.state = .Downloaded
                }
            }
            else
            {
                imageRecord.state = .Failed
                imageRecord.image = UIImage(named: "Failed")
            }
        } catch {
            return
        }
    }
    
}
