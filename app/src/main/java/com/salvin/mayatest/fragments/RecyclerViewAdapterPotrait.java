package com.salvin.mayatest.fragments;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.salvin.mayatest.R;
import com.salvin.mayatest.feed.Content;


import java.util.List;


public class RecyclerViewAdapterPotrait extends RecyclerView.Adapter<RecyclerViewAdapterPotrait.FeedViewHolder> {

    //Sent params
    List<Content> contents;
    Activity activity;
    ImageLoader imageLoader;


    static final int TYPE_CELL = 1;
    private int lastPosition = -1;


    public RecyclerViewAdapterPotrait(List<Content> contents, Activity activity) {
        this.contents = contents;
        this.activity = activity;
        imageLoader = ImageLoader.getInstance();


    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CELL;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = null;

        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_card_small, viewGroup, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, final int position) {

        SetName(holder, position);
        SetTimestamp(holder, position);
        SetStatusMsg(holder, position);
        SetUrl(holder, position);
        SetProfilePic(holder, position);
        SetFeedImageView(holder, position);
        setAnimation(holder.cv, position);

    }

    private void SetFeedImageView(FeedViewHolder holder, int position) {
        holder.feedImageView.setImageDrawable(null);
        if (contents.get(position).getImage() != null) {
            holder.feedImageView.setVisibility(View.VISIBLE);
            imageLoader.displayImage(contents.get(position).getImage(), holder.feedImageView);
        } else
            holder.feedImageView.setVisibility(View.GONE);
    }

    private void SetProfilePic(FeedViewHolder holder, int position) {
        holder.profilePic.setImageDrawable(null);
        if (contents.get(position).getProfilePic() != null) {
            holder.profilePic.setVisibility(View.VISIBLE);
            imageLoader.displayImage(contents.get(position).getProfilePic(), holder.profilePic);
        } else
            holder.profilePic.setVisibility(View.GONE);

    }

    private void SetUrl(FeedViewHolder holder, int position) {
        if (contents.get(position).getUrl() != null) {
            holder.url.setText(Html.fromHtml("<a href=\"" + contents.get(position).getUrl() + "\">"
                    + contents.get(position).getUrl() + "</a> "));

            // Making url clickable
            holder.url.setMovementMethod(LinkMovementMethod.getInstance());
            holder.url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.url.setVisibility(View.GONE);
        }
    }

    private void SetStatusMsg(FeedViewHolder holder, int position) {
        if (!TextUtils.isEmpty(contents.get(position).getStatus())) {
            holder.statusMsg.setText(contents.get(position).getStatus());
            holder.statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMsg.setVisibility(View.GONE);
        }
    }

    private void SetTimestamp(FeedViewHolder holder, int position) {
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(contents.get(position).getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timestamp.setText(timeAgo);
    }

    private void SetName(FeedViewHolder holder, int position) {
        String title = contents.get(position).getName();
        holder.name.setText(title);
    }

    private void setAnimation(CardView viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    class FeedViewHolder extends RecyclerView.ViewHolder {

        protected TextView name ;
        protected TextView timestamp;
        protected TextView statusMsg;
        protected TextView url;
        protected ImageView profilePic;
        protected ImageView feedImageView;

        protected CardView cv;

        public FeedViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.card_view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(activity, R.anim.lift_on_touch);
                cv.setStateListAnimator(stateListAnimator);
            }

             name = (TextView) view.findViewById(R.id.name);
             timestamp = (TextView) view.findViewById(R.id.timestamp);
             statusMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
             url = (TextView) view.findViewById(R.id.txtUrl);
             profilePic = (ImageView) view.findViewById(R.id.profilePic);
             feedImageView = (ImageView) view.findViewById(R.id.feedImage1);

        }
    }

}

