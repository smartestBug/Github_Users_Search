package dev.msemyak.gitusersearch.mvp.model.remote;

import java.util.List;

import dev.msemyak.gitusersearch.mvp.model.local.UserFull;
import dev.msemyak.gitusersearch.mvp.model.local.UserRepo;
import dev.msemyak.gitusersearch.mvp.model.local.UserSearchResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubAPI {

    @GET("search/users")
    Single<UserSearchResponse> searchUsers(@Query("q") String token, @Query("page") int page);

    @GET("users/{userName}")
    Single<UserFull> getUser(@Path("userName") String username);

    @GET("users/{userName}/repos?per_page=100")
    Single<List<UserRepo>> getUserRepos(@Path("userName") String username);

}
