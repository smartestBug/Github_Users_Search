package dev.msemyak.gitusersearch.mvp.model.remote;

import dev.msemyak.gitusersearch.mvp.model.local.UserSearchResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubAPI {

    @GET("search/users")
    Single<UserSearchResponse> searchUsers(@Query("q") String token);

}