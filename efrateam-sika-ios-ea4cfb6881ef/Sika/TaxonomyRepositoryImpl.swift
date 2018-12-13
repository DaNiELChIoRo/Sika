//
//  TaxonomyRepositoryImpl.swift
//  Sika
//
//  Created by Aldo Antonio Martinez Avalos on 25/07/17.
//  Copyright © 2017 Sika. All rights reserved.
//

import Foundation

class TaxonomyRepositoryImpl: TaxonomyRepository {

    let taxonomyWebService = TaxonomyWebService()
    let suggestionsWebService = SuggestionsWebService()
    let slidersWebService = SliderWebService()
    let glossaryWebService = GlossaryWebService()
    
    func getSuggestions(completion: @escaping SuggestionsResponseClosure) {
        suggestionsWebService.getSuggestions() {
            response in
            var suggestions: [SuggestionCategory] = []
            switch response{
            case .success(let response):
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject> {
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let suggestion = SuggestionCategory(JSONString: String(describing: json)) {
                                suggestions.append(suggestion)
                            }
                        }
                    } catch {
                        //completion(false, [])
                    }
                }
                completion(true, suggestions)
                break
            case .failure: completion(false, [])
                break
            case .notConnectedToInternet : completion(false, [])
                break
            }
        }
    }
    
    func getTaxonomies(completion: @escaping TaxonomyResponseClosure) {
        self.taxonomyWebService.getTaxonomies() {
            response in
            var taxonomies: [Taxonomy] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let state = Taxonomy(JSONString: String(describing: json)) {
                                taxonomies.append(state)
                            }
                        }
                    } catch {
                        completion(false, taxonomies)
                    }
                }
                completion(true, taxonomies)
                break
            case .failure: completion(false, taxonomies)
                break
            case .notConnectedToInternet : completion(false, taxonomies)
                break
            }

        }
    }
    
    func getSliders(completion: @escaping SlidersResponseClosure) {
        slidersWebService.getSliders(completion: {
            response in
            var sliders: [SliderItem] = []
            switch response {
            case .success(let response) :
                for obj in response {
                    do{
                        if let dic = obj as? Dictionary<String, AnyObject>{
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let slideritem = SliderItem(JSONString: String(describing: json)) {
                                sliders.append(slideritem)
                            }
                        }
                    } catch {
                        completion(false, sliders)
                    }
                }
                completion(true, sliders)
            case .failure: completion(false, sliders)
            case .notConnectedToInternet: completion(false, sliders)
            }
        })
    }
    
    func getGlossary(completion: @escaping GlossaryResponseClosure) {
        glossaryWebService.getGlossary(completion: {
            response in
            switch response {
            case .success(let response):
                var glossary: [GlossaryItem] = []
                for element in response {
                    if let dic = element as? Dictionary<String, AnyObject> {
                        do {
                            let data = try JSONSerialization.data(withJSONObject: dic , options: [])
                            if let json = String(data: data,encoding: String.Encoding.utf8), let glossaryItem = GlossaryItem(JSONString: String(describing: json)) {
                                glossary.append(glossaryItem)
                            }
                        } catch {
                            completion(false, [])
                        }
                    }
                }
                completion(!glossary.isEmpty, glossary)
            case .failure: completion(false, [])
            case .notConnectedToInternet : completion(false, [])
            }
        })
    }
    
    func cancelAllRequest() {
        
    }

}
