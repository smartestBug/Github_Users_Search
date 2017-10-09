package dev.msemyak.gitusersearch.base;

import android.view.View;

import java.util.List;

import dev.msemyak.gitusersearch.mvp.model.local.UserBrief;

public interface BaseView {

    void showMessage(String message);
    void showMessage(int stringId);

    interface MainView extends BaseView{
        void showUsers(List<UserBrief> usersList);
        void showWaitingScreen();
        void showUsersScreen();
        void showErrorScreen();
        void notifyLoadingDone();
        void notifyAdapterDataChange();
        void notifyAdapterItemInserted(int position);
        void notifyAdapterItemRemoved(int position);
        void scrollRecyclerViewToPosition(int position);
        void setAuxText(String msg);

    }

    interface UserDetailsView extends BaseView{
        void showWaitingScreen();
        void showUserDetailsScreen(String userName, String avatarUrl, String userEmail, String userLocation,
                             String userBio, String userCreated, String userFollowers,
                             String userFollowing, String userRepos, String userRepoNames);
        void showErrorScreen();

    }

    interface RVItemClickListener extends BaseView {
        void onRVItemClick(View v, String username, String avatarUrl);
    }
}
