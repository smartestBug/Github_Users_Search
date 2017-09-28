package dev.msemyak.gitusersearch.base;

public class BasePresenterImpl<T> {

    public T myView;

    public BasePresenterImpl(T myView) {
        this.myView = myView;
    }

}
