package dev.msemyak.gitusersearch.mvp.model.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.github.com/";
    private static GitHubAPI gitAPI = null;
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofit()
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static GitHubAPI getGitHubAPI() {

        if (gitAPI == null) {
            gitAPI = getRetrofit().create(GitHubAPI.class);
        }

        return gitAPI;
    }



}
