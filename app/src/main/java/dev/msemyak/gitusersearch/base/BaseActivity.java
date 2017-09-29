package dev.msemyak.gitusersearch.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    public T myPresenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);

        myPresenter = getPresenter();
    }

    protected abstract int getLayoutId();
    protected abstract T getPresenter();

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }

    public void showWaitDialog(String message) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();

    }

    public void showWaitDialog(int stringId) {
        showWaitDialog(getString(stringId));
    }

    public void dismissWaitDialog() {
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPresenter.unsubscribeObservers();
    }
}
