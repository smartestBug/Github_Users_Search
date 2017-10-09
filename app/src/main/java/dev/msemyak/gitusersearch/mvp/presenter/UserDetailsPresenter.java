package dev.msemyak.gitusersearch.mvp.presenter;

import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BasePresenterImpl;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.NetworkEngine;
import dev.msemyak.gitusersearch.mvp.model.local.UserFullAndRepos;
import dev.msemyak.gitusersearch.mvp.model.local.UserRepo;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class UserDetailsPresenter extends BasePresenterImpl<BaseView.UserDetailsView> implements BasePresenter.UserDetailsPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public UserDetailsPresenter(BaseView.UserDetailsView view) {
        super(view);
    }

    @Override
    public void loadUserDetails(String username) {

        myView.showWaitingScreen();

        //get user info and repos
        subscriptions.add(
                Single.zip(
                        NetworkEngine.getUser(username),
                        NetworkEngine.getUserRepos(username)
                                .flatMapObservable(Observable::fromIterable)
                                .map(UserRepo::getRepoName)
                                .reduce((s, s2) -> s + "\n" + s2)
                                .toSingle(),
                        UserFullAndRepos::new)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                user -> myView.showUserDetailsScreen(user.userData.getName(), user.userData.getAvatarUrl(),
                                        user.userData.getEmail(), user.userData.getLocation(), user.userData.getBio(),
                                        user.userData.getCreatedAt(), user.userData.getFollowers().toString(),
                                        user.userData.getFollowing().toString(),
                                        user.userData.getPublicRepos().toString(),
                                        user.userRepos),
                                error -> {
                                    myView.showErrorScreen();
                                    error.printStackTrace();
                                }
                        ));
    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
