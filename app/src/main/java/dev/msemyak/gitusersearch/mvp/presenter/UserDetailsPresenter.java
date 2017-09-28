package dev.msemyak.gitusersearch.mvp.presenter;

import java.util.Arrays;
import java.util.List;

import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BasePresenterImpl;
import dev.msemyak.gitusersearch.base.BaseView;

import dev.msemyak.gitusersearch.mvp.model.NetworkEngine;
import dev.msemyak.gitusersearch.mvp.model.local.UserFull;
import dev.msemyak.gitusersearch.mvp.model.local.UserFullAndRepos;
import dev.msemyak.gitusersearch.mvp.model.local.UserRepo;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static dev.msemyak.gitusersearch.utils.Logg.Logg;


public class UserDetailsPresenter extends BasePresenterImpl<BaseView.UserDetailsView> implements BasePresenter.UserDetailsPresenter {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public UserDetailsPresenter(BaseView.UserDetailsView view) {
        super(view);
    }


    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
