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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.email_edt)
    EditText emailEdt;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private String email, password;
    private boolean isEmailValid, isPasswordValid, isUserAuthorized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        isEmailValid = false;
        isPasswordValid = false;
        isUserAuthorized = false;
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        email = emailEdt.getText().toString();
        if (email == null) {
            emailEdt.setError("Please Enter Valid Email");
        }
        password = passwordEdt.getText().toString();
        if (password == null) {
            passwordEdt.setError("Please Enter Valid Error");
        }

        if (email != null && password != null) {
            if (Utils.isValidEmail(email)) {
                isEmailValid = true;
            } else {
                emailEdt.setError("Enter Valid Email");
            }

            if (Utils.isValidPassword(password)) {
                isPasswordValid = true;
            } else {
                passwordEdt.setError("Password is Less than 6");
            }

            if (isEmailValid && isPasswordValid) {
                //do login
                Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser user) {
                        // user has been logged in
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "User Authorized", Toast.LENGTH_SHORT).show();
                            if ((boolean) user.getProperty(Keys.User.IS_ADMIN)) {
                                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                LoginActivity.this.finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), VotingActivity.class));
                                LoginActivity.this.finish();
                            }
                        } else {
                            Log.i(TAG, "User: User is not fetched");
                        }
                    }

                    public void handleFault(BackendlessFault fault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.i(TAG, "login failed in LoginActivity: " + fault.getMessage());
                    }
                });
            }
        } else {

            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
        }
    }
}
