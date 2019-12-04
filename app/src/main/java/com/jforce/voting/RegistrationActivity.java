package com.jforce.voting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.jforce.voting.api.Keys;
import com.jforce.voting.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.username_edt)
    EditText usernameEdt;
    @BindView(R.id.phone_edt)
    EditText phoneEdt;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.email_edt)
    EditText emailEdt;

    private String name, email, phone, password;
    private boolean isEmailValid, isPhoneValid, isPasswordValid, isUserRegistered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        isEmailValid = false;
        isPhoneValid = false;
        isPasswordValid = false;

        // TODO: 02-12-2019 Check is any Backendless User Instance is there if yes -> check if user voted if yes-> goto votedactivity.
        //                                                                     no -> goto Voting Activity.

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.register_btn)
    public void onRegisterBtnClicked() {
        name = usernameEdt.getText().toString();
        if (name == null) {
            usernameEdt.setError("Please Fill candidateName");
        }
        email = emailEdt.getText().toString();
        if (email == null) {
            emailEdt.setError("Please Fill Email");
        }
        phone = phoneEdt.getText().toString();
        if (phone == null) {
            phoneEdt.setError("Please Fill Phone");
        }
        password = passwordEdt.getText().toString();
        if (password == null) {
            passwordEdt.setError("Please Fill Password");
        }

        if (name != null && email != null && phone != null && password != null) {
            //validate email
            if (Utils.isValidEmail(email)) {
                isEmailValid = true;
            } else {
                emailEdt.setError("Enter Valid Email");
            }
            //validate phone
            if (Utils.isValidPhoneNumber(phone)) {
                isPhoneValid = true;
            } else {
                phoneEdt.setError("Enter valid Phone");
            }
            //validate password
            if (Utils.isValidPassword(password)) {
                isPasswordValid = true;
            } else {
                passwordEdt.setError("Only Allowed Greater Than 6 Charter");
            }
            //check all details
            if (isEmailValid && isPhoneValid && isPasswordValid) {
                //perform registration;
                BackendlessUser user = new BackendlessUser();
                user.setProperty(Keys.User.NAME, name);
                user.setProperty(Keys.User.EMAIL, email);
                user.setPassword(password);

                Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser registeredUser) {
                        // user has been registered and now can login
                        if (registeredUser != null) {
                            isUserRegistered = true;
                            Log.i(TAG, "handleResponse: User Registration Successful" + user.getEmail());
                        }
                    }

                    public void handleFault(BackendlessFault fault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                });
                if (isUserRegistered) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    this.finish();
                }
            }

        } else {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.login_btn)
    public void onLoginBtnClicked() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}
