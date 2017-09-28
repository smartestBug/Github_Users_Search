package dev.msemyak.gitusersearch.mvp.model;

import dev.msemyak.gitusersearch.mvp.model.local.UserSearchResponse;
import dev.msemyak.gitusersearch.mvp.model.remote.RetrofitClient;
import io.reactivex.Single;

public class NetworkEngine {

    public static Single<UserSearchResponse> searchUsers(String searchString) {

        return RetrofitClient.getGitHubAPI().searchUsers(searchString);
    }

}
