package dev.msemyak.gitusersearch.base;

import java.util.List;

import dev.msemyak.gitusersearch.mvp.model.local.User;

public interface BaseView {

    void showMessage(String message);
    void showMessage(int stringId);

    void showWaitDialog(String message);
    void showWaitDialog(int stringId);
    void dismissWaitDialog();

    interface MainView extends BaseView{

        void showUsers(List<User> usersList);

    }


}
