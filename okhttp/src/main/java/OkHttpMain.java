import okhttp3.*;

import java.io.IOException;

public class OkHttpMain {
    public static void main(String[] args) {
        get("https://www.baidu.com");
    }

    private static void get(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        System.out.println("请求已经发送");
    }

    private static void post(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(new FormBody.Builder().build()).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


}
