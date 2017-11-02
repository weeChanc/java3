package com.xt.java3.ui.login;

import com.xt.java3.User;
import com.xt.java3.base.BaseView;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;

/**
 * Created by steve on 17-10-22.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void onLoginStart();
        void onLoginSuccess(User user);
        void onLoginError(Throwable e);
        void onStateCallback(User user);
    }

    interface Presenter {
         void login(LoginBody request);
         void judgeState();
    }
}
