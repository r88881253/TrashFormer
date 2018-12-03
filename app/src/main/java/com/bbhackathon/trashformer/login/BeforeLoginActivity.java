package com.bbhackathon.trashformer.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.chooseRecycleCategory.ChooseRecycleCategoryActivity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.google.firebase.auth.FirebaseUser;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class BeforeLoginActivity extends BaseActivity {

    LoginManager mLoginmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);
//        initGif(R.id.beforeLoginGif);
        setStatusBar(R.color.yellow_background_F6C946);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkLogin();
    }

    private void initGif(int gifImageViewId){
        GifImageView gifImageView = (GifImageView) findViewById(gifImageViewId);

        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.test);

            // gif1加载一个动态图gif
            gifImageView.setImageDrawable(gifDrawable);

        } catch (Exception e) {
            e.printStackTrace();
        }

        playGif(gifImageView);
    }

    public void playGif(final GifImageView gifImageView){
        Animation fadeout = new AlphaAnimation(1.f, 1.f);
        fadeout.setDuration(2500); // You can modify the duration here
        fadeout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkLogin();
            }
        });
        gifImageView.startAnimation(fadeout);
    }

    private void checkLogin(){
        mLoginmanager = new LoginManager(this.getApplicationContext());

        FirebaseUser user = FirebaseAuthManager.getInstance().getUser();

        if(user != null){
            if(mLoginmanager.getRecycleCategory() == null || mLoginmanager.getRecycleCategory().isEmpty()){
                Intent intent = new Intent(BeforeLoginActivity.this, ChooseRecycleCategoryActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(BeforeLoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(BeforeLoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
