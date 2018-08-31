package com.instaclonegram.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.instaclonegram.R;
import com.instaclonegram.adapters.FeedListViewAdapter;
import com.instaclonegram.models.Photo;
import com.instaclonegram.models.User;
import com.like.LikeButton;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lamine on 29/03/2016.
 */
public class FragmentFeed extends Fragment {

    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    private static String username = "kevin";
    FeedListViewAdapter flva;
    User user;

    Firebase firebase;
    public FragmentFeed() {

    }

    public FragmentFeed(Firebase firebase, User user) {
        this.firebase = firebase;
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        final ListView lv = (ListView)rootView.findViewById(R.id.feed_listView);

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.attachToListView(lv);

        final ArrayList<Photo> al = new ArrayList<>();
        final ArrayList<String> ids = new ArrayList<>();

        Firebase ref = new Firebase("https://instaclonegram.firebaseio.com/images");
        Query queryRef = ref.orderByChild("timeStamp");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                ids.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String snap_id = postSnapshot.getKey();
                    Photo photo = postSnapshot.getValue(Photo.class);
                    Log.d("snap", snap_id);
                    al.add(photo);
                    ids.add(snap_id);
                }
                Collections.reverse(al);
                Collections.reverse(ids);
                flva = new FeedListViewAdapter(getContext(), R.layout.photo_item, al, ids, firebase, FragmentFeed.this);
                lv.setAdapter(flva);
                flva.clear();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Collections.reverse(al);
                Collections.reverse(ids);
                al.add(snapshot.getValue(Photo.class));
                ids.add(snapshot.getKey());
                Collections.reverse(al);
                Collections.reverse(ids);
                flva = new FeedListViewAdapter(getContext(), R.layout.photo_item, al, ids, firebase, FragmentFeed.this);
                lv.setAdapter(flva);
                //lv.getAdapter()
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ref.removeEventListener(this);
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
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(rootView);
                flva.notifyDataSetChanged();
                //firebase.child("image").child(bmp.get).setValue(imageFile);
            }
        });

        /*FeedListViewAdapter flva = new FeedListViewAdapter(getContext(), R.layout.photo_item, al, firebase);
        lv.setAdapter(flva);*/
        return rootView;
    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public String convertImage()
    {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                /* ENCODED */

                String converted = convertImage();
                Random rand = new Random();
                int randomNum = rand.nextInt(1000001);
                Long tsLong = System.currentTimeMillis()/1000;
                String timestamp = tsLong.toString();


                Photo new_pic = new Photo(converted, fileName.replace(".", "o"),randomNum, user.getUsername(), 0,
                        timestamp, bitmap.getHeight(), bitmap.getWidth());

                firebase.child("images").push().setValue(new_pic);
                //flva.notifyDataSetChanged();


            } else {
                Toast.makeText(this.getActivity().getApplicationContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.d("exception", e.toString());
        }

    }


}
