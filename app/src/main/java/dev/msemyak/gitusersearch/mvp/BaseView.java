package dev.msemyak.gitusersearch.mvp;

public interface BaseView {

    void showMessage(String message);
    void showMessage(int stringId);

    void showWaitDialog(String message);
    void showWaitDialog(int stringId);
    void dismissWaitDialog();

    interface MainView extends BaseView{

    }


}
