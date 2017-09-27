package dev.msemyak.gitusersearch.mvp.view;

import android.os.Bundle;

import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.mvp.BaseActivity;
import dev.msemyak.gitusersearch.mvp.BaseView;

import static dev.msemyak.gitusersearch.utils.Logg.Logg;

public class MainActivity extends BaseActivity implements BaseView.MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logg("#### Something to log!");
        Logg("### and formatted %s", R.layout.activity_main);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
