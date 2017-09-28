package dev.msemyak.gitusersearch.mvp.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BaseActivity;
import dev.msemyak.gitusersearch.base.BasePresenter;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.local.User;
import dev.msemyak.gitusersearch.mvp.presenter.MainActivityPresenter;

import static android.support.v7.recyclerview.R.attr.layoutManager;
import static dev.msemyak.gitusersearch.utils.Logg.Logg;

public class MainActivity extends BaseActivity<BasePresenter.MainActivityPresenter> implements BaseView.MainView, RVItemClickListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_main) RecyclerView recyclerViewMain;

    private RVAdapterUsers listAdapter = null;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("GitHub users search");

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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Logg("###### Changed text: " + newText);
        return false;
    }

    @Override
    public void showUsers(List<User> usersList) {

        if (listAdapter == null) {

            layoutManager = new LinearLayoutManager(this);
            recyclerViewMain.setLayoutManager(layoutManager);
            listAdapter = new RVAdapterUsers(usersList, this, this);
            recyclerViewMain.setAdapter(listAdapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMain.getContext(), layoutManager.getOrientation());
            recyclerViewMain.addItemDecoration(dividerItemDecoration);

        }
        else {
            listAdapter.setNewData(usersList);
            listAdapter.notifyDataSetChanged();
            //recyclerViewMain.smoothScrollToPosition(usersList.size()-1);
        }

    }

    @Override
    public void onRVItemClick(View v, String username) {
        showMessage("User was clicked: " + username);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPresenter.unsubscribeObservers();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainActivityPresenter getPresenter() {
        return new MainActivityPresenter(this);
    }
}
