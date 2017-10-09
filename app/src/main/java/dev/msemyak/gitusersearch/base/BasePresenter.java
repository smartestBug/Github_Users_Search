package dev.msemyak.gitusersearch.base;

public interface BasePresenter {

    void unsubscribeObservers();

    interface MainActivityPresenter extends BasePresenter{

        void getUsersAndDisplay(String query);
        void loadMoreUsers();
    }

    interface UserDetailsPresenter extends BasePresenter{
        void loadUserDetails(String username);
    }

}