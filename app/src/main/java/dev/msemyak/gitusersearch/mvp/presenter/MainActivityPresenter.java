package dev.msemyak.gitusersearch.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

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

import static dev.msemyak.gitusersearch.utils.Logg.Logg;

public class MainActivityPresenter extends BasePresenterImpl<BaseView.MainView> implements BasePresenter.MainActivityPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    List<UserBrief> usersList;
    int page;
    int usersNumber;
    String query;

    public MainActivityPresenter(BaseView.MainView view) {
        super(view);

        usersList = new ArrayList<>();
        page = 1;
        usersNumber = 0;
    }

    @Override
    public void getUsersAndDisplay(String query) {

        myView.showWaitDialog("Fetching users...");
        this.query = query;
        usersList.clear();
        page = 1;
        usersNumber = 0;

        Logg("Showing dialog");

        subscriptions.add(
                NetworkEngine.searchUsers(query, page)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        response -> {
                            myView.dismissWaitDialog();

                           usersList = response.getUsers();

                            myView.showUsers(usersList);
                            //myView.showMessage("Total users found matching search criteria: " + response.getTotalCount());
                            usersNumber = response.getTotalCount();
                            updateAux();
                        },
                        error -> {
                            myView.dismissWaitDialog();
                            myView.showMessage("Error loading users!");
                            error.printStackTrace();
                        }
                ));

    }

    @Override
    public void loadMoreUsers() {
        myView.showWaitDialog("Fetching users...");
        Logg("Showing dialog for more users");

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
                                    myView.showMessage("Error loading more users! Query: " + query + " Page: " + page);
                                    error.printStackTrace();
                                }
                        ));

    }

    @Override
    public void loadSpecificUser(String userName) {

        myView.showWaitDialog("Fetching user..");

        //get user info and repos
        subscriptions.add(
                Single.zip(
                        NetworkEngine.getUser(userName),
                        NetworkEngine.getUserRepos(userName),
                        (userFull, userRepos) -> new UserFullAndRepos(userFull, userRepos))
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                user -> {
                                    myView.dismissWaitDialog();

                                    StringBuilder repoString = new StringBuilder();
                                    for (UserRepo uR : user.userRepos) {
                                        repoString.append(uR.getRepoName());
                                        repoString.append("\n");
                                    }

                                    Logg("Location: " + user.userData.getLocation());

                                    myView.openUserDetails(user.userData.getName(), user.userData.getAvatarUrl(),
                                            user.userData.getEmail(), user.userData.getLocation(), user.userData.getBio(),
                                            user.userData.getCreatedAt(), user.userData.getFollowers().toString(),
                                            user.userData.getFollowing().toString(),
                                            user.userData.getPublicRepos().toString(),
                                            repoString.toString());
                                },
                                error -> {
                                    myView.dismissWaitDialog();
                                    myView.showMessage("Error loading user information!");
                                    error.printStackTrace();
                                }
                        ));
    }

    private void updateAux() {
        myView.setAuxText("Page: " + page + "   Users displayed: " + usersList.size() + " / " + usersNumber);
    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
