package com.instaclonegram;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.instaclonegram.fragments.FragmentLogin;
import com.instaclonegram.fragments.FragmentRegister;

import java.util.Map;

/**
 * Created by lamine on 12/04/2016.
 */
public class LoginActivity extends Activity {

    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, new FragmentLogin()).commit();
    }
}
