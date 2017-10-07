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
import dev.msemyak.gitusersearch.mvp.model.local.UserFullAndRepos;
import dev.msemyak.gitusersearch.mvp.model.local.UserRepo;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static dev.msemyak.gitusersearch.utils.Logg.Logg;

public class MainActivityPresenter extends BasePresenterImpl<BaseView.MainView> implements BasePresenter.MainActivityPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private List<UserBrief> usersList;
    private int page;
    private int usersNumber;
    private String query;
    boolean loadingMoreUsers;

    public MainActivityPresenter(BaseView.MainView view) {
        super(view);

        usersList = new ArrayList<>();
        page = 1;
        usersNumber = 0;
        loadingMoreUsers = false;
    }

    @Override
    public void getUsersAndDisplay(String query) {

        //myView.showWaitDialog(R.string.fetch_users);
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

                                    //myView.dismissWaitDialog();

                                    usersList = response.getUsers();

                                    myView.showUsersScreen();
                                    myView.showUsers(usersList);

                                    usersNumber = response.getTotalCount();
                                    updateAux();
                                },
                                error -> {
                                    //myView.dismissWaitDialog();
                                    myView.showErrorScreen();
                                    myView.showMessage(R.string.error_loading_user);
                                    error.printStackTrace();
                                }
                        ));

    }

    @Override
    public void loadMoreUsers() {

        Logg("Loading more users");
        //myView.showWaitDialog(R.string.fetch_users);
        if (!loadingMoreUsers) {
            loadingMoreUsers = true;

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
                                    //myView.dismissWaitDialog();
                                    usersList.remove(usersList.size()-1);
                                    myView.notifyAdapterItemRemoved(usersList.size());

                                    myView.showUsersScreen();
                                    usersList.addAll(response.getUsers());
                                    myView.notifyAdapterDataChange();
                                    updateAux();
                                    loadingMoreUsers = false;
                                },
                                error -> {
                                    //myView.dismissWaitDialog();
                                    usersList.remove(usersList.size()-1);
                                    myView.notifyAdapterItemRemoved(usersList.size());
                                    myView.showMessage(R.string.error_loading_more_users);
                                    error.printStackTrace();
                                    loadingMoreUsers = false;
                                }
                        ));
        }

    }

    @Override
    public void loadSpecificUser(String userName) {

        myView.showWaitDialog(R.string.fetch_user);

        //get user info and repos
        subscriptions.add(
                Single.zip(
                        NetworkEngine.getUser(userName),
                        NetworkEngine.getUserRepos(userName)
                                .flatMapObservable(Observable::fromIterable)
                                .map(UserRepo::getRepoName)
                                .reduce((s, s2) -> s + "\n" + s2)
                                .toSingle(),
                        UserFullAndRepos::new)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                user -> {
                                    myView.dismissWaitDialog();

                                    myView.openUserDetails(user.userData.getName(), user.userData.getAvatarUrl(),
                                            user.userData.getEmail(), user.userData.getLocation(), user.userData.getBio(),
                                            user.userData.getCreatedAt(), user.userData.getFollowers().toString(),
                                            user.userData.getFollowing().toString(),
                                            user.userData.getPublicRepos().toString(),
                                            user.userRepos);
                                },
                                error -> {
                                    myView.dismissWaitDialog();
                                    myView.showMessage(R.string.error_loading_user_info);
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
