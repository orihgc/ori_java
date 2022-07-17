import retrofit2.Call;
import retrofit2.http.GET;

public interface RpcService {

    @GET(value = "/")
    Call<Object> fetchData();
}
