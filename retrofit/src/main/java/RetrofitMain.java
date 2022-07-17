import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

public class RetrofitMain {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls()
                        .serializeSpecialFloatingPointValues()
                        .disableHtmlEscaping().setLenient().create()))
                .build();

        RpcService rpcService = retrofit.create(RpcService.class);
        Call<Object> objectCall = rpcService.fetchData();
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                System.out.println("failed");
            }
        });

    }
}
