package dev.msemyak.gitusersearch.base;

public interface BasePresenter {

    void unsubscribeObservers();

    interface MainActivityPresenter extends BasePresenter{

        void getUsersAndDisplay(String query);
        void loadMoreUsers();
        void loadSpecificUser(String username);
    }

    interface UserDetailsPresenter extends BasePresenter{

    }

}