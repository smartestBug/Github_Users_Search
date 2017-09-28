package dev.msemyak.gitusersearch.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.utils.DelayedProgressDialog;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    public T myPresenter;

    private ProgressDialog progressDialog;
    //DelayedProgressDialog progressDialog;

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
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int stringId) {
        //Snackbar.make(findViewById(R.id.constraintLayout), getString(stringId), BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }

    public void showWaitDialog(String message) {

        //progressDialog = new DelayedProgressDialog();
        //progressDialog.show(getSupportFragmentManager(), "tag");


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
