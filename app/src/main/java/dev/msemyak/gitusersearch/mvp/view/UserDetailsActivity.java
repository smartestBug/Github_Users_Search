package dev.msemyak.gitusersearch.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.BindView;
import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BaseActivity;
import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.presenter.UserDetailsPresenter;
import dev.msemyak.gitusersearch.utils.GlideApp;

public class UserDetailsActivity extends BaseActivity<BasePresenter.UserDetailsPresenter> implements BaseView.UserDetailsView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;
    @BindView(R.id.tv_user_name) TextView tvUserName;
    @BindView(R.id.tv_user_email) TextView tvUserEmail;
    @BindView(R.id.tv_user_location) TextView tvUserLocation;
    @BindView(R.id.tv_user_bio) TextView tvUserBio;
    @BindView(R.id.tv_user_created) TextView tvUserCreated;
    @BindView(R.id.tv_user_followers) TextView tvUserFollowers;
    @BindView(R.id.tv_user_following) TextView tvUserFollowing;
    @BindView(R.id.tv_user_repos) TextView tvUserRepos;
    @BindView(R.id.tv_user_repos_list) TextView tvUserReposList;

    @BindView(R.id.iv_user_location) ImageView ivUserLocation;
    @BindView(R.id.iv_user_email) ImageView ivUserEmail;
    @BindView(R.id.iv_user_bio) ImageView ivUserBio;

    @BindView(R.id.view_flipper_user_details) ViewFlipper viewFlipper;
    @BindView(R.id.constraint_layout_user_details) ConstraintLayout clUserDetails;

    String userName;
    String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = getIntent().getStringExtra("username");
        avatarUrl = getIntent().getStringExtra("avatar_url");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(userName);

        GlideApp.with(this)
                .load(avatarUrl)
                .centerCrop()
                .into(toolbarImage);

        myPresenter.loadUserDetails(userName);

    }

    @Override
    public void showWaitingScreen() {
        showScreen(R.id.user_details_loading);
    }

    @Override
    public void showUserDetailsScreen(String userName, String avatarUrl, String userEmail, String userLocation, String userBio, String userCreated, String userFollowers, String userFollowing, String userRepos, String userRepoNames) {
        showScreen(R.id.user_details_screen);
        tvUserName.setText(userName);

        if (userEmail == null) {
            ivUserEmail.setVisibility(View.GONE);
            tvUserEmail.setVisibility(View.GONE);
        }
        else tvUserEmail.setText(userEmail);

        if (userLocation == null) {
            ivUserLocation.setVisibility(View.GONE);
            tvUserLocation.setVisibility(View.GONE);
        }
        else tvUserLocation.setText(userLocation);

        if (userBio == null) {
            ivUserBio.setVisibility(View.GONE);
            tvUserBio.setVisibility(View.GONE);
        }
        else tvUserBio.setText(userBio);

        tvUserCreated.setText(userCreated);
        tvUserFollowers.setText(userFollowers);
        tvUserFollowing.setText(userFollowing);
        tvUserRepos.setText(userRepos);
        tvUserReposList.setText(userRepoNames);

        clUserDetails.requestLayout();
    }

    @Override
    public void showErrorScreen() {
        showScreen(R.id.user_details_loading_error);
    }

    void showScreen(int screen_id) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(viewFlipper.findViewById(screen_id)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_details;
    }

    @Override
    protected BasePresenter.UserDetailsPresenter getPresenter() {
        return new UserDetailsPresenter(this);
    }
}
