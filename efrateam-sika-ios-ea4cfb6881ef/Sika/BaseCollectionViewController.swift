//
//  BaseCollectionViewController.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 8/9/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class BaseCollectionViewController: BaseViewController {
    
    let pendingOperations = PendingOperations()

    func startOperationsForPhotoRecord(photoDetails: ImageRecord, indexPath: IndexPath){
        switch (photoDetails.state) {
        case .New:
            startDownloadForRecord(photoDetails: photoDetails, indexPath: indexPath)
        default:
            break
        }
    }
    
    func reloadIndexPaths(indexPaths: [IndexPath]){
    
    }
    
    func reloadIndexes(indexes: [Int]) {
    
    }
    
    func startDownloadForRecord(photoDetails: ImageRecord, indexPath: IndexPath){
        guard pendingOperations.downloadsInProgress[indexPath] == nil else {
            return
        }
        let downloader = ImageDownloader(imageRecord: photoDetails)
        downloader.completionBlock = {
            if downloader.isCancelled {
                return
            }
            DispatchQueue.main.async {
                self.pendingOperations.downloadsInProgress.removeValue(forKey: indexPath)
                self.reloadIndexPaths(indexPaths: [indexPath])
            }
        }
        pendingOperations.downloadsInProgress[indexPath] = downloader
        pendingOperations.downloadQueue.addOperation(downloader)
    }
    
    func startDownloadForRecord(photoDetails: ImageRecord, index: Int){
        guard pendingOperations.downloadsQueued[index] == nil else {
            return
        }
        let downloader = ImageDownloader(imageRecord: photoDetails)
        downloader.completionBlock = {
            if downloader.isCancelled {
                return
            }
            DispatchQueue.main.async {
                self.pendingOperations.downloadsQueued.removeValue(forKey: index)
                self.reloadIndexes(indexes: [index])
            }
        }
        pendingOperations.downloadsQueued[index] = downloader
        pendingOperations.downloadQueue.addOperation(downloader)
    }
    
}
