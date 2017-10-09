package dev.msemyak.gitusersearch.mvp.presenter;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BasePresenterImpl;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.NetworkEngine;
import dev.msemyak.gitusersearch.mvp.model.local.UserBrief;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.disposables.CompositeDisposable;

import static dev.msemyak.gitusersearch.utils.Logg.Logg;

public class MainActivityPresenter extends BasePresenterImpl<BaseView.MainView> implements BasePresenter.MainActivityPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private List<UserBrief> usersList;
    private int page;
    private int usersNumber;
    private String query;
    private boolean loadingMoreUsers;

    public MainActivityPresenter(BaseView.MainView view) {
        super(view);

        usersList = new ArrayList<>();
        page = 1;
        usersNumber = 0;
        loadingMoreUsers = false;
    }

    @Override
    public void getUsersAndDisplay(String query) {

        myView.showWaitingScreen();

        this.query = query;
        usersList.clear();
        page = 1;
        usersNumber = 0;

        subscriptions.add(
                NetworkEngine.searchUsers(query, page)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                response -> {

                                    usersList = response.getUsers();

                                    myView.showUsersScreen();
                                    myView.showUsers(usersList);

                                    usersNumber = response.getTotalCount();
                                    updateAux();
                                },
                                error -> {
                                    myView.showErrorScreen();
                                    error.printStackTrace();
                                }
                        ));

    }

    @Override
    public void loadMoreUsers() {

        if (usersNumber <=30) {
            myView.notifyLoadingDone();
            return;
        }

        int position = usersList.size();

        Handler handler = new Handler();

        final Runnable r = () -> {
            usersList.add(position, null);
            myView.notifyAdapterItemInserted(position);
            myView.scrollRecyclerViewToPosition(position);
        };

        handler.post(r);

        subscriptions.add(
                NetworkEngine.searchUsers(query, page + 1)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                response -> {
                                    page++;

                                    usersList.remove(usersList.size() - 1);
                                    myView.notifyAdapterItemRemoved(usersList.size());
                                    myView.notifyLoadingDone();

                                    myView.showUsersScreen();
                                    usersList.addAll(response.getUsers());
                                    myView.notifyAdapterDataChange();
                                    updateAux();
                                },
                                error -> {
                                    usersList.remove(usersList.size() - 1);
                                    myView.notifyAdapterItemRemoved(usersList.size());

                                    myView.showMessage(R.string.error_loading_more_users);
                                    error.printStackTrace();
                                }
                        ));
    }


    private void updateAux() {
        myView.setAuxText(" " + usersList.size() + " / " + usersNumber);
    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
