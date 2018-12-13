//
//  String.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 27/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation
import UIKit

extension String {

    func localized() -> String {
        return NSLocalizedString(self, comment: "")
    }
    
    var htmlAttributedString: NSAttributedString? {
        
        do {
            let htmlStyle = "<style>p {font-family:\(Font.FontName.OpenSansLight); font-size:\(14.0)px;} body {font-family:\(Font.FontName.OpenSansFamily); font-size:\(14.0)px;}}</style>"
            let htmlString = "\(htmlStyle)\n\n\(self)"
            return try NSAttributedString(data: htmlString.data(using: .utf8)!, options: [
                NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType,
                NSCharacterEncodingDocumentAttribute: String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch let error {
            print(error.localizedDescription)
        }
        return nil
    }
    
    
    func height(withConstrainedWidth width: CGFloat, font: UIFont) -> CGFloat {
        let constraintRect = CGSize(width: width, height: .greatestFiniteMagnitude)
        let boundingBox = self.boundingRect(with: constraintRect, options: .usesLineFragmentOrigin, attributes: [NSFontAttributeName: font], context: nil)
        
        return boundingBox.height
    }
    
    func width(withConstraintedHeight height: CGFloat, font: UIFont) -> CGFloat {
        let constraintRect = CGSize(width: .greatestFiniteMagnitude, height: height)
        let boundingBox = self.boundingRect(with: constraintRect, options: .usesLineFragmentOrigin, attributes: [NSFontAttributeName: font], context: nil)
        
        return boundingBox.width
    }

    func htmlAttributedString(fontSize: CGFloat = 15.0) -> NSAttributedString? {
        let fontName = UIFont.systemFont(ofSize: fontSize).fontName
        let string = self.appending(String(format: "<style>body{font-family: '%@'; font-size:%fpx;}</style>", fontName, fontSize))
        guard let data = string.data(using: String.Encoding.utf16, allowLossyConversion: false) else { return nil }
        guard let html = try? NSMutableAttributedString (
            data: data,
            options: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8.rawValue],
            documentAttributes: nil) else { return nil }
        return html
    }
    
    func htmlFormat() -> NSAttributedString? {
        guard let htmlData = NSString(string: self).data(using: String.Encoding.unicode.rawValue) else {
            return nil
        }
        let options = [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8.rawValue] as [String : Any]
        
        do {
            let attributedString = try NSAttributedString(data: htmlData, options: options, documentAttributes: nil)
            return attributedString
        } catch {
            return nil
        }
    }
    
}
