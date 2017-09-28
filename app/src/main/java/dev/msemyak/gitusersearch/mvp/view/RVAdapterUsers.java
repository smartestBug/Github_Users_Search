package dev.msemyak.gitusersearch.mvp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.msemyak.gitusersearch.R;
import dev.msemyak.gitusersearch.mvp.model.local.User;
import dev.msemyak.gitusersearch.utils.GlideApp;


class RVAdapterUsers extends RecyclerView.Adapter<RVAdapterUsers.myViewHolder> {

    private List<User> usersList;
    private Context context;
    private RVItemClickListener clickListener;

    RVAdapterUsers(List<User> usersList, Context context, RVItemClickListener clickListener) {
        this.usersList = usersList;
        this.context = context;
        this.clickListener = clickListener;
    }

    void setNewData(List<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public RVAdapterUsers.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_user_info_item, parent, false);
        return new myViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        User userForDataBind = usersList.get(position);

        GlideApp.with(context)
                .load(userForDataBind.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        holder.tvUsername.setText(userForDataBind.getLogin());
        holder.tvScore.setText(userForDataBind.getScore().toString());
        holder.username = userForDataBind.getLogin();
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivAvatar;
        TextView tvUsername;
        TextView tvScore;
        String username;
        RVItemClickListener clickListener;

        myViewHolder(View v, RVItemClickListener clickListener) {
            super(v);
            ivAvatar = v.findViewById(R.id.iv_avatar);
            tvUsername = v.findViewById(R.id.tv_username);
            tvScore = v.findViewById(R.id.tv_score);
            this.clickListener = clickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onRVItemClick(view, username);
        }
    }

}


