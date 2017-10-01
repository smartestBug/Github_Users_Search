package dev.msemyak.gitusersearch.base;

import android.view.View;

import java.util.List;

import dev.msemyak.gitusersearch.mvp.model.local.UserBrief;

public interface BaseView {

    void showMessage(String message);
    void showMessage(int stringId);
    void showWaitDialog(String message);
    void showWaitDialog(int stringId);
    void dismissWaitDialog();

    interface MainView extends BaseView{
        void showUsers(List<UserBrief> usersList);
        void showWaitingScreen();
        void showUsersScreen();
        void showErrorScreen();
        void notifyAdapterDataChange();
        void setAuxText(String msg);
        void openUserDetails(String userName, String avatarUrl, String userEmail, String userLocation,
                        String userBio, String userCreated, String userFollowers,
                        String userFollowing, String userRepos, String userRepoNames);
    }

    interface UserDetailsView extends BaseView{

    }

    interface RVItemClickListener extends BaseView {
        void onRVItemClick(View v, String username, String avatarUrl);
    }
}
