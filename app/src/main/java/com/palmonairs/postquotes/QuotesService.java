package com.palmonairs.postquotes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface QuotesService {
    @POST("api/quotes/find/")
    Call<ResponseData> getQuotes(@Body Payload data);
}
