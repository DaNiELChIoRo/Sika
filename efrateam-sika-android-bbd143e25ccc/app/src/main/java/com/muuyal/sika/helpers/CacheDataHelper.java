package com.muuyal.sika.helpers;

import com.muuyal.sika.utils.DateUtils;
import com.muuyal.sika.utils.TextUtils;

import java.util.HashMap;

/**
 * Created by isra on 8/29/17.
 */

public class CacheDataHelper {

    private static CacheDataHelper _instance;
    private HashMap<String, String> cacheUrls;

    public static CacheDataHelper getInstance() {
        if (_instance == null)
            _instance = new CacheDataHelper();

        return _instance;
    }

    public CacheDataHelper() {
        cacheUrls = new HashMap<>();
    }

    public void saveUrl(String url, String currentDate) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(currentDate))
            cacheUrls.put(url, currentDate);
    }

    public boolean existUrl(String url) {
        return cacheUrls != null && !TextUtils.isEmpty(url) && cacheUrls.containsKey(url);
    }

    public boolean expiredUrl(String url) {
        if (cacheUrls != null && !TextUtils.isEmpty(url)) {
            if (existUrl(url) && !DateUtils.isExpired(cacheUrls.get(url))) {
                return false;
            } else {
                saveUrl(url, DateUtils.getActualTimeComplete());
                return true;
            }
        }
        return true;
    }

}
