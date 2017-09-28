package dev.msemyak.gitusersearch.mvp.model.local;

import java.util.List;

public class UserFullAndRepos {

    public UserFull userData;
    public List<UserRepo> userRepos;

    public UserFullAndRepos(UserFull userData, List<UserRepo> userRepos) {
        this.userData = userData;
        this.userRepos = userRepos;
    }
}
