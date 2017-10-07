package dev.msemyak.gitusersearch.mvp.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.base.BaseView;
import dev.msemyak.gitusersearch.mvp.model.local.UserBrief;
import dev.msemyak.gitusersearch.utils.GlideApp;


class RVAdapterUsers extends RecyclerView.Adapter<RVAdapterUsers.BaseViewHolder> {

    private List<UserBrief> usersList;
    private Context context;
    private BaseView.RVItemClickListener clickListener;

    private static final int ITEM_USER = 1;
    private static final int ITEM_WAIT = 0;

    @IntDef({ITEM_USER, ITEM_WAIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface itemType {
    }

    RVAdapterUsers(List<UserBrief> usersList, Context context, BaseView.RVItemClickListener clickListener) {
        this.usersList = usersList;
        this.context = context;
        this.clickListener = clickListener;
    }

    void setNewData(List<UserBrief> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int getItemViewType(int position) {
        return usersList.get(position) == null ? ITEM_WAIT : ITEM_USER;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, @itemType int viewType) {

        BaseViewHolder vh;

        if (viewType == ITEM_USER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_user_info_item, parent, false);
            vh = new myViewHolder(v, viewType, clickListener);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_loading_spinner, parent, false);
            vh = new myProgressHolder(v, viewType);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder someHolder, int position) {

        if (someHolder.getItemType() == ITEM_USER) {

            myViewHolder holder = (myViewHolder) someHolder;

            UserBrief userForDataBind = usersList.get(position);

            GlideApp.with(context)
                    .load(userForDataBind.getAvatarUrl())
                    .circleCrop()
                    .into(holder.ivAvatar);

            holder.tvUsername.setText(userForDataBind.getLogin());
            holder.tvScore.setText(userForDataBind.getScore().toString());
            holder.username = userForDataBind.getLogin();
            holder.avatarUrl = userForDataBind.getAvatarUrl();
        } else {
            myProgressHolder holder = (myProgressHolder) someHolder;
            holder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    // --- inner classes

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        @itemType
        private int itemType;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }

    static class myViewHolder extends BaseViewHolder implements View.OnClickListener {

        ImageView ivAvatar;
        TextView tvUsername;
        TextView tvScore;
        String username;
        String avatarUrl;

        BaseView.RVItemClickListener clickListener;

        myViewHolder(View v, @itemType int itemType, BaseView.RVItemClickListener clickListener) {
            super(v);
            this.setItemType(itemType);
            ivAvatar = v.findViewById(R.id.iv_avatar);
            tvUsername = v.findViewById(R.id.tv_username);
            tvScore = v.findViewById(R.id.tv_score);
            this.clickListener = clickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onRVItemClick(view, username, avatarUrl);
        }
    }

    static class myProgressHolder extends BaseViewHolder {

        ProgressBar progressBar;

        myProgressHolder(View v, @itemType int itemType) {
            super(v);
            this.setItemType(itemType);
            progressBar = v.findViewById(R.id.rv_progress_bar);
        }
    }

}


