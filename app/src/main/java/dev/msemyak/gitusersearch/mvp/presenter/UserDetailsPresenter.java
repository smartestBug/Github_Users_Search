package dev.msemyak.gitusersearch.mvp.presenter;

import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BasePresenterImpl;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.utils.RxUtil;
import io.reactivex.disposables.CompositeDisposable;

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
