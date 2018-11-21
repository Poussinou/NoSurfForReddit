package com.aaronhalbert.nosurfforreddit.adapters;

import androidx.lifecycle.LiveData;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.aaronhalbert.nosurfforreddit.R;
import com.aaronhalbert.nosurfforreddit.fragments.LinkPostFragment;
import com.aaronhalbert.nosurfforreddit.fragments.SelfPostFragment;
import com.aaronhalbert.nosurfforreddit.viewmodel.MainActivityViewModel;
import com.aaronhalbert.nosurfforreddit.viewmodel.PostsFragmentViewModel;
import com.aaronhalbert.nosurfforreddit.viewstate.LastClickedPostMetadata;
import com.aaronhalbert.nosurfforreddit.viewstate.PostsViewState;
import com.aaronhalbert.nosurfforreddit.databinding.RowBinding;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.RowHolder> {

    // we only ever show the first page of posts, which is 25 by default
    private static final int ITEM_COUNT = 25;

    private final MainActivityViewModel mainActivityViewModel;
    private final Fragment hostFragment;
    private final LiveData<PostsViewState> postsViewStateLiveData;

    /* this app has two primary screens/modes, a feed of posts from r/all (Reddit's public home
     * page, and a feed of posts from the user's subscribed subreddits (if the user is logged in).
     *
     * any field/method referring to "AllPosts" refers to the former, and any field/method
     * referring to "SubscribedPosts" refers to the latter.
     *
     * Many components, such as this adapter, are easily reused for either feed. For example,
     * all that's necessary to configure this adapter is to pass it the boolean argument
     * isSubscribedPostsAdapter in the constructor, and it sets own its data source
     * (postsViewStateLiveData) and functions accordingly. */
    private final boolean isSubscribedPostsAdapter;

    public PostsAdapter(PostsFragmentViewModel viewModel,
                        MainActivityViewModel mainActivityViewModel,
                        Fragment hostFragment,
                        boolean isSubscribedPostsAdapter) {

        this.mainActivityViewModel = mainActivityViewModel;
        this.hostFragment = hostFragment;
        this.isSubscribedPostsAdapter = isSubscribedPostsAdapter;

        if (isSubscribedPostsAdapter) {
            postsViewStateLiveData = viewModel.getSubscribedPostsViewStateLiveData();
        } else {
            postsViewStateLiveData = viewModel.getAllPostsViewStateLiveData();
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBinding rowBinding = RowBinding
                .inflate(hostFragment.getLayoutInflater(), parent, false);

        /* follow the view hierarchy lifecycle of hostFragment instead of its fragment lifecycle.
         * If we follow the fragment lifecycle, the row.xml data binding class does not correctly
         * release its reference to its row controller (RowHolder) when PostsFragment is
         * detached upon being replace()'d. This results in a PostAdapter being leaked on each
         * RecyclerView click, which in turn leads to numerous DTO and Glide objects also
         * being leaked */
        rowBinding.setLifecycleOwner(hostFragment.getViewLifecycleOwner());

        return new RowHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(RowHolder rowHolder, int position) {
        rowHolder.bindModel();
    }

    // region helper classes -----------------------------------------------------------------------

    public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RowBinding rowBinding;

        RowHolder(RowBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
            itemView.setOnClickListener(this);
        }

        void bindModel() {
            rowBinding.setController(this);
            rowBinding.executePendingBindings();
        }

        //placed here so data binding class can access it
        public LiveData<PostsViewState> getPostsViewStateLiveData() {
            return postsViewStateLiveData;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            mainActivityViewModel.setLastClickedPostMetadata(new LastClickedPostMetadata(
                    position,
                    postsViewStateLiveData.getValue().postData.get(position).id,
                    postsViewStateLiveData.getValue().postData.get(position).isSelf,
                    postsViewStateLiveData.getValue().postData.get(position).url,
                    isSubscribedPostsAdapter));

            if (postsViewStateLiveData.getValue().postData.get(position).isSelf) {
                Navigation.findNavController(v).navigate(R.id.fragment_self_post_dest);
            } else {
                Navigation.findNavController(v).navigate(R.id.fragment_link_post_dest);
            }
        }
    }

    // endregion helper classes---------------------------------------------------------------------
}
