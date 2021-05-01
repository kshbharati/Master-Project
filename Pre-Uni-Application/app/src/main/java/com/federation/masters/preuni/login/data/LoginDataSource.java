package com.federation.masters.preuni.login.data;

import android.util.Log;

import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.models.UserDetail;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    UserDetail userDetail = new UserDetail(1, "Kshitiz", "Bharati", "0401375989", 1, 1);
    User user = new User(1, "henge", userDetail);


    public Result<User> login(String email, String password) {
        if (email.equals(user.getEmail()) && password.equals("testpassword")) {
            Log.d("HELLO", "Response: ");
            try {

                User loggedUser = user;
                return new Result.Success<>(loggedUser);
            } catch (Exception e) {
                return new Result.Error(new IOException("Error logging in", e));
            }
        } else {
            return new Result.Failed<>("Invalid Email/Password");
        }

    }

    public void logout() {
        // TODO: revoke authentication
    }
}