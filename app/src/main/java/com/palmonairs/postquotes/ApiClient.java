package com.palmonairs.postquotes;

public class ApiClient {
    private ApiClient() {}

    public static final String BASE_URL = "https://api-quotes.herokuapp.com/";

    public static QuotesService getQuotesService() {
        return RetrofitClient.getClient(BASE_URL).create(QuotesService.class);
    }
}
