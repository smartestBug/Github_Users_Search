package dev.msemyak.gitusersearch.mvp.presenter;

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
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivityPresenter extends BasePresenterImpl<BaseView.MainView> implements BasePresenter.MainActivityPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private List<UserBrief> usersList;
    private int page;
    private int usersNumber;
    private String query;

    public MainActivityPresenter(BaseView.MainView view) {
        super(view);

        usersList = new ArrayList<>();
        page = 1;
        usersNumber = 0;
    }

    @Override
    public void getUsersAndDisplay(String query) {

        myView.showWaitDialog(R.string.fetch_users);
        this.query = query;
        usersList.clear();
        page = 1;
        usersNumber = 0;

        subscriptions.add(
                NetworkEngine.searchUsers(query, page)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        response -> {
                            myView.dismissWaitDialog();

                           usersList = response.getUsers();

                            myView.showUsers(usersList);

                            usersNumber = response.getTotalCount();
                            updateAux();
                        },
                        error -> {
                            myView.dismissWaitDialog();
                            myView.showMessage(R.string.error_loading_user);
                            error.printStackTrace();
                        }
                ));

    }

    @Override
    public void loadMoreUsers() {
        myView.showWaitDialog(R.string.fetch_users);

        subscriptions.add(
                NetworkEngine.searchUsers(query, page+1)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                response -> {
                                    page++;
                                    myView.dismissWaitDialog();
                                    usersList.addAll(response.getUsers());
                                    myView.notifyAdapterDataChange();
                                    updateAux();
                                },
                                error -> {
                                    myView.dismissWaitDialog();
                                    myView.showMessage(R.string.error_loading_more_users);
                                    error.printStackTrace();
                                }
                        ));

    }

    @Override
    public void loadSpecificUser(String userName) {

        myView.showWaitDialog(R.string.fetch_user);

        //get user info and repos
        subscriptions.add(
                Single.zip(
                        NetworkEngine.getUser(userName),
                        NetworkEngine.getUserRepos(userName),
                        UserFullAndRepos::new)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                user -> {
                                    myView.dismissWaitDialog();

                                    //ouch( I'll fix this, I promise
                                    StringBuilder repoString = new StringBuilder();
                                    for (UserRepo uR : user.userRepos) {
                                        repoString.append(uR.getRepoName());
                                        repoString.append("\n");
                                    }

                                    myView.openUserDetails(user.userData.getName(), user.userData.getAvatarUrl(),
                                            user.userData.getEmail(), user.userData.getLocation(), user.userData.getBio(),
                                            user.userData.getCreatedAt(), user.userData.getFollowers().toString(),
                                            user.userData.getFollowing().toString(),
                                            user.userData.getPublicRepos().toString(),
                                            repoString.toString());
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
