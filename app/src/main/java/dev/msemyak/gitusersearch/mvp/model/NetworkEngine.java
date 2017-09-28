package dev.msemyak.gitusersearch.mvp.model;

import java.util.List;

import dev.msemyak.gitusersearch.mvp.model.local.UserFull;
import dev.msemyak.gitusersearch.mvp.model.local.UserRepo;
import dev.msemyak.gitusersearch.mvp.model.local.UserSearchResponse;
import dev.msemyak.gitusersearch.mvp.model.remote.RetrofitClient;
import io.reactivex.Single;

public class NetworkEngine {

    public static Single<UserSearchResponse> searchUsers(String searchString, int page) {
        return RetrofitClient.getGitHubAPI().searchUsers(searchString, page);
    }

    public static Single<UserFull> getUser(String userName) {
        return RetrofitClient.getGitHubAPI().getUser(userName);
    }

    public static Single<List<UserRepo>> getUserRepos(String userName) {
        return RetrofitClient.getGitHubAPI().getUserRepos(userName);
    }

}
