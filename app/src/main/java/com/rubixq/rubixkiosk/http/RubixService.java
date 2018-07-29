package com.rubixq.rubixkiosk.http;

import com.rubixq.rubixkiosk.models.Customer;
import com.rubixq.rubixkiosk.models.Queue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RubixService {
    @GET("/queues")
    Call<List<Queue>> getQueues();

    @POST("/customers")
    Call<Customer> createCustomer(@Body Customer customer);
}
