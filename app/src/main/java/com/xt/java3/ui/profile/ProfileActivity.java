package com.xt.java3.ui.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.Constant;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.base.BaseActivity;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.ui.main.frag.contacts.ContactsPresenter;
import com.xt.java3.util.BitmapUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends BaseActivity {



    @BindView(R.id.add)
    Button button;

    @BindView(R.id.head)
    ImageView head;

    @BindView(R.id.nickname)
    TextView nickname;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null){

                    App.client.modifyFriends(Constant.ACTION_ADD,user.getId()).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<BaseResponse>() {
                                @Override
                                public void accept(BaseResponse response) throws Exception {
                                    if (response.getStatus() == 0) {
                                        EventBus.getDefault().post(ContactsPresenter.ADD_FRIENDS);
                                        ToastUtils.showShort("添加成功");
                                    } else if(response.getStatus() == -1) {
                                        ToastUtils.showShort("不可重复添加");
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    ToastUtils.showShort(throwable.toString());
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //接受选择的人的资料,并显示UI
    @Subscribe(threadMode = ThreadMode.MAIN,sticky =  true)
    public void getUser(User user){
        this.user = user;
        head.setImageBitmap(BitmapUtils.base64ToBitmap(user.getAvatar()));
        nickname.setText(user.getNickname());
    }
}
