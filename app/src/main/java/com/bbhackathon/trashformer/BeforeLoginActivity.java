package com.bbhackathon.trashformer;

import android.content.Intent;
import android.media.session.MediaController;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.bbhackathon.trashformer.Manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.Manager.LoginManager;
import com.bbhackathon.trashformer.login.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class BeforeLoginActivity extends AppCompatActivity {

    LoginManager mLoginmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);

//        GifImageView gifImageView = (GifImageView) findViewById(R.id.beforeLoginGif);
//
//
//        try {
//            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
//            // 将gif图资源转化为GifDrawable
//            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.test);
//
//            // gif1加载一个动态图gif
//            gifImageView.setImageDrawable(gifDrawable);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        playGif(gifImageView);



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
            Intent intent = new Intent(BeforeLoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(BeforeLoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
