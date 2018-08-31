package com.instaclonegram.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.instaclonegram.MainActivity;
import com.instaclonegram.R;

import java.util.Map;

/**
 * Created by lamine on 12/04/2016.
 */
public class FragmentRegister extends Fragment {
    Firebase ref;

    public FragmentRegister() {

    }

    public FragmentRegister(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText et_email_reg = (EditText)rootView.findViewById(R.id.et_email_register);
        final EditText et_pwd_reg = (EditText)rootView.findViewById(R.id.et_password_register);
        final EditText et_username_reg = (EditText)rootView.findViewById(R.id.et_username_register);
        Button btn_signup = (Button)rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_reg.getText() != null && et_pwd_reg.getText() != null
                        && et_username_reg.getText() != null) {
                    ref.createUser(et_email_reg.getText().toString(), et_pwd_reg.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Intent myIntent = new Intent(getActivity(), MainActivity.class);
                            myIntent.putExtra("process", "registerSimple");
                            myIntent.putExtra("email", et_email_reg.getText().toString()); //Optional parameters
                            myIntent.putExtra("username", et_username_reg.getText().toString());
                            myIntent.putExtra("name", et_username_reg.getText().toString());
                            myIntent.putExtra("picture", "http://static1.squarespace.com/static/509951a5e4b07a4c81b1172d/530f94a0e4b024271b3502f5/530f950ae4b0340ce5280dba/1456342011053/Kevin_Systrom_Cover.jpg");
                            getActivity().startActivity(myIntent);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            // there was an error
                            Log.d("HELLO", firebaseError.getDetails());
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Fill the sign up form!", Toast.LENGTH_LONG);
                }
            }
        });



        return rootView;
    }
}
