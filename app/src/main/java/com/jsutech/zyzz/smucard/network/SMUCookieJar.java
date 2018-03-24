package com.jsutech.zyzz.smucard.network;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zyzz on 3/23/18.
 *
 */

public class SMUCookieJar implements CookieJar {
    private Map<String, List<Cookie>> cookieStore;

    public SMUCookieJar(){
        cookieStore = new HashMap<>();
    }

    /**
     * Saves {@code cookies} from an HTTP response to this store according to this jar's policy.
     * <p>
     * <p>Note that this method may be called a second time for a single HTTP response if the response
     * includes a trailer. For this obscure HTTP feature, {@code cookies} contains only the trailer's
     * cookies.
     *
     * @param url
     * @param cookies
     */
    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        // 先去重
        Set<Cookie> tmp = new HashSet<>(cookies);
        cookies.clear();
        cookies.addAll(tmp);
        cookieStore.put(url.host(), cookies);
    }


    /**
     * Load cookies from the jar for an HTTP request to {@code url}. This method returns a possibly
     * empty list of cookies for the network request.
     * <p>
     * <p>Simple implementations will return the accepted cookies that have not yet expired and that
     * {@linkplain Cookie#matches match} {@code url}.
     *
     * @param url
     */
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null? cookies : new ArrayList<Cookie>();
    }

    public void clearCookies(){
        cookieStore.clear();
    }

    public void deleteCookie(String host, String cookieName){
        List<Cookie> cookies = cookieStore.get(host);
        if (cookies != null){
            Iterator<Cookie> cookieIterator = cookies.iterator();
            while (cookieIterator.hasNext()){
                Cookie c = cookieIterator.next();
                if (c.name().equals(cookieName)){
                    cookieIterator.remove();
                }
            }
        }
    }

    public Cookie getCookie(String host, String cookieName){
        List<Cookie> cookies = cookieStore.get(host);
        if (cookies != null){
            for (Cookie c : cookies) {
                if (c.name().equals(cookieName)) {
                    return c;
                }
            }
            return null;

        } else {
            return null;
        }
    }

    public List<Cookie> getCookies(String host){
        return cookieStore.get(host);
    }

    public void setCookie(String host, Cookie cookie){
        List<Cookie> cookies = cookieStore.get(host);
        if (cookies == null){
            cookies = new ArrayList<>();
        }
        int pos = cookies.indexOf(cookie);
        if (pos > -1){
            cookies.set(pos, cookie);
        } else {
            cookies.add(cookie);
        }
    }
}
