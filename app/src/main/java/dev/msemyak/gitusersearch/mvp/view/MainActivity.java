package dev.msemyak.gitusersearch.mvp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import butterknife.BindView;
import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BaseActivity;
import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.local.UserBrief;
import dev.msemyak.gitusersearch.mvp.presenter.MainActivityPresenter;


public class MainActivity extends BaseActivity<BasePresenter.MainActivityPresenter> implements BaseView.MainView, BaseView.RVItemClickListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_aux) TextView tvAux;
    @BindView(R.id.rv_main) RecyclerView recyclerViewMain;
    @BindView(R.id.view_flipper) ViewFlipper viewFlipper;

    private RVAdapterUsers listAdapter = null;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_title_for_toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        myPresenter.getUsersAndDisplay(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showUsers(List<UserBrief> usersList) {

        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(viewFlipper.findViewById(R.layout.incl_main_screen_layout)));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        if (listAdapter == null) {

            layoutManager = new LinearLayoutManager(this);
            recyclerViewMain.setLayoutManager(layoutManager);
            listAdapter = new RVAdapterUsers(usersList, this, this);
            recyclerViewMain.setAdapter(listAdapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMain.getContext(), layoutManager.getOrientation());
            recyclerViewMain.addItemDecoration(dividerItemDecoration);

            recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1)) {
                        myPresenter.loadMoreUsers();
                    }
                }
            });

        }
        else {
            listAdapter.setNewData(usersList);
            notifyAdapterDataChange();
            recyclerViewMain.scrollToPosition(0);
        }

    }

    @Override
    public void openUserDetails(String userName, String avatarUrl, String userEmail, String userLocation, String userBio, String userCreated, String userFollowers, String userFollowing, String userRepos, String userRepoNames) {
        Intent userDetailsIntent = new Intent(getApplicationContext(), UserDetailsActivity.class);
        userDetailsIntent.putExtra("username", userName);
        userDetailsIntent.putExtra("avatar_url", avatarUrl);
        userDetailsIntent.putExtra("email", userEmail);
        userDetailsIntent.putExtra("location", userLocation);
        userDetailsIntent.putExtra("bio", userBio);
        userDetailsIntent.putExtra("created", userCreated);
        userDetailsIntent.putExtra("followers", userFollowers);
        userDetailsIntent.putExtra("following", userFollowing);
        userDetailsIntent.putExtra("repos", userRepos);
        userDetailsIntent.putExtra("repos_names", userRepoNames);
        startActivity(userDetailsIntent);
    }

    @Override
    public void notifyAdapterDataChange() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAuxText(String msg) {
        String auxString = getString(R.string.users_displayed) + msg;
        tvAux.setText(auxString);
    }

    @Override
    public void onRVItemClick(View v, String username, String avatarUrl) {
        myPresenter.loadSpecificUser(username);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter.MainActivityPresenter getPresenter() {
        return new MainActivityPresenter(this);
    }
}
