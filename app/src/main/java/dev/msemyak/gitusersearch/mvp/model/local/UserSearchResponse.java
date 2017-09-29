package dev.msemyak.gitusersearch.mvp.model.local;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSearchResponse {
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<UserBrief> items = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<UserBrief> getUsers() {
        return items;
    }

}
