package dev.msemyak.gitusersearch.mvp.model.local;

import java.util.List;

public class UserFullAndRepos {

    public UserFull userData;
    public String userRepos;

    public UserFullAndRepos(UserFull userData, String userRepos) {
        this.userData = userData;
        this.userRepos = userRepos;
    }
}
