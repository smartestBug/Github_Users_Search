package dev.msemyak.gitusersearch.mvp.presenter;

import java.util.List;

import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BasePresenterImpl;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.NetworkEngine;
import dev.msemyak.gitusersearch.mvp.model.local.User;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivityPresenter extends BasePresenterImpl<BaseView.MainView> implements BasePresenter.MainActivityPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public MainActivityPresenter(BaseView.MainView view) {
        super(view);
    }

    @Override
    public void getUsersAndDisplay(String query) {

        myView.showWaitDialog("Fetching data");

        subscriptions.add(
                NetworkEngine.searchUsers(query)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        response -> {
                            myView.dismissWaitDialog();

                            List<User> userList = response.getUsers();
                            myView.showUsers(userList);
                            myView.showMessage("Total users found matching search criteria: " + response.getTotalCount());
                            /*
                            for (int i = 0; i< userList.size(); i++) {
                                User u = userList.get(i);
                                Logg("------------------- USER# " + (i+1) + " ---------------------------");
                                Logg("###### -> User ID: %s", u.getId());
                                Logg("###### -> User Login/name: %s", u.getLogin());
                                Logg("###### -> User Avatar Id: %s", u.getAvatarUrl());
                                Logg("###### -> User Repos URL: %s", u.getReposUrl());
                            }
                            */
                        },
                        error -> {
                            myView.dismissWaitDialog();
                            error.printStackTrace();
                        }
                ));

    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
