package com.hongan.template.base.utils;

import okhttp3.OkHttpClient;

public class OkHttp {
    private final static OkHttpClient Client = new OkHttpClient();
    public static OkHttpClient getClient() {
        return Client;
    }
}
