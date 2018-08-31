package com.instaclonegram.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.util.LruCache;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.instaclonegram.MainActivity;
import com.instaclonegram.R;
import com.instaclonegram.fragments.FragmentExplore;
import com.instaclonegram.fragments.FragmentFeed;
import com.instaclonegram.fragments.FragmentProfile;
import com.instaclonegram.fragments.FragmentRegister;
import com.instaclonegram.library.AsyncDrawable;
import com.instaclonegram.library.BitmapWorkerTask;
import com.instaclonegram.library.GetBitmapAsyncTask;
import com.instaclonegram.models.Photo;
import com.instaclonegram.models.User;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by lamine on 09/04/2016.
 */

public class FeedListViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Photo> data = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private Firebase firebase;
    private int new_photo_height;
    private int screen_width;
    private FragmentFeed fragmentFeed;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static LruCache<String, Bitmap> mMemoryCache;
    final int cacheSize = maxMemory / 2;


    public FeedListViewAdapter(Context context, int layoutResourceId, ArrayList data, ArrayList ids, Firebase firebase, FragmentFeed fragmentFeed) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.ids = ids;
        this.firebase = firebase;
        this.fragmentFeed = fragmentFeed;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        final Photo photo = data.get(position);
        final String str = ids.get(position);
        Log.d("CURRENT GETVIEW ID", str);
        final Firebase currentRef = firebase.child("images").child(str);
        final Map<String, Object> likemap = new HashMap<>();
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        screen_width = displayMetrics.widthPixels;
        new_photo_height = (screen_width * photo.getHeight()) / photo.getWidth();

        Log.d("Viewing", Integer.toString(data.get(position).getId()));
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.profile_pic = (CircleImageView)row.findViewById(R.id.feed_profile_imgview);
            holder.username = (TextView) row.findViewById(R.id.feed_tv_username);
            holder.username.setTextColor(ContextCompat.getColor(getContext(), R.color.instagramblue));
            holder.timestamp = (TextView)row.findViewById(R.id.feed_tv_timestamp);
            holder.timestamp.setTextColor(ContextCompat.getColor(getContext(), R.color.greycolor1));
            holder.image = (ImageView) row.findViewById(R.id.feed_imageView);
            holder.like_button = (LikeButton)row.findViewById(R.id.feed_heart_button);
            holder.like_cnt = (TextView)row.findViewById(R.id.feed_tv_likes_cnt);
            holder.like_cnt.setTextColor(ContextCompat.getColor(getContext(), R.color.instagramblue));

            holder.like_tv = (TextView)row.findViewById(R.id.feed_tv_likes);
            holder.like_tv.setTextColor(ContextCompat.getColor(getContext(), R.color.instagramblue));

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        final ViewHolder finalHolder = holder;
        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                finalHolder.like_cnt.setText(String.valueOf(snapshot.child("like").getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                finalHolder.like_cnt.setText("0");
            }
        });

        holder.like_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likemap.put("like", photo.getLike() + 1);
                        photo.setLike(photo.getLike() + 1);
                        currentRef.updateChildren(likemap);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                //Log.d("CURRENT ID", ids.get(position));
                //finalHolder.like_cnt.setText(String.valueOf(photo.getLike()));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likemap.put("like", photo.getLike() - 1);
                        photo.setLike(photo.getLike() - 1);
                        currentRef.updateChildren(likemap);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });


        /*Bitmap bitmap = base64ToBitmap(data.get(position));
        holder.image.setImageBitmap(bitmap);
        holder.image.setMinimumWidth(screen_width);
        holder.image.setMinimumHeight(new_photo_height);*/

        loadBitmap(holder.image, data.get(position));

        setProfilePicFromUsername(photo.getUsername(), holder);

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(photo.getTimeStamp()), System.currentTimeMillis()
                , DateUtils.SECOND_IN_MILLIS);
        holder.timestamp.setText(timeAgo);
        holder.username.setText(photo.getUsername());
        return row;
    }

    static class ViewHolder {
        CircleImageView profile_pic;
        TextView username;
        TextView timestamp;
        ImageView image;
        LikeButton like_button;
        TextView like_cnt;
        TextView like_tv;
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
            Log.d("ADD BITMAP TO CACHE", key);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        Log.d("GET BITMAP FROM CACHE", key);
        return mMemoryCache.get(key);
    }

    private static Bitmap base64ToBitmap(Photo current) {
        byte[] decodedString = Base64.decode(current.getPhoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void loadBitmap(ImageView imageView, Photo photo) {
        if (cancelPotentialWork(photo.getPhoto(), imageView)) {
            final String imageKey = String.valueOf(photo.getId());
            //final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            //if (bitmap != null) {
            //    imageView.setImageBitmap(bitmap);
            //} else {
                Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.item_placeholder);
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(getContext().getResources(), bm, task);
                imageView.setMinimumWidth(screen_width);
                imageView.setMinimumHeight(new_photo_height);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(photo.getPhoto(), String.valueOf(new_photo_height), String.valueOf(screen_width), String.valueOf(photo.getId()));
           // }
        }
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData.contentEquals("") || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private void setProfilePicFromUsername(String username, final ViewHolder holder) {
        Firebase.setAndroidContext(this.getContext());
        final Firebase ref = new Firebase("https://instaclonegram.firebaseio.com/users");
        Query queryRef = ref.orderByChild("username").equalTo(username);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                final User user = dataSnapshot.getValue(User.class);
                holder.username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //final User user = dataSnapshot.getValue(User.class);
                        final Context context = fragmentFeed.getContext();
                        ((MaterialNavigationDrawer)fragmentFeed.getActivity()).setFragment(new FragmentProfile(firebase, user), user.getName());
                    }
                });

                String profilePic = user.getPicture();
                Bitmap profilePicBitmap = null;
                try {
                    profilePicBitmap = new GetBitmapAsyncTask().execute(profilePic).get();
                    //profilePicBitmap.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                holder.profile_pic.setImageBitmap(profilePicBitmap);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



}
