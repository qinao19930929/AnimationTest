package animationtest.qin.com.animationtest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import view.MdStyleProgress;

public class MainActivity extends AppCompatActivity {
    TextView tvTest;
    RelativeLayout rlRoot;
    MdStyleProgress progress;
    int parentHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);
        rlRoot = findViewById(R.id.rl_root);
        progress = findViewById(R.id.progress);


        rlRoot.post(new Runnable() {

            @Override
            public void run() {
                parentHeight = rlRoot.getMeasuredWidth();
            }
        });
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startAnimation(tvTest, parentHeight, new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//                    }
//                });
                progress.startAnimator();
            }
        });

        findViewById(R.id.btn_to_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,DialogActivity.class));
            }
        });
    }

    private void startAnimation(View tartgetView, int parentHeight, Animator.AnimatorListener listener) {
        //下半部分的动画
        ObjectAnimator bottomPart = ObjectAnimator.ofFloat(tartgetView, "translationY", parentHeight / 2, 0).setDuration(800);
        ObjectAnimator bottomPartAlPha = ObjectAnimator.ofFloat(tartgetView, "alpha", 0, 1f).setDuration(700);
        //上半部分的动画
        ObjectAnimator topPart = ObjectAnimator.ofFloat(tartgetView, "translationY", 0, 0 - parentHeight / 2).setDuration(800);
        ObjectAnimator topPartAlPha = ObjectAnimator.ofFloat(tartgetView, "alpha", 1f, 0f).setDuration(700);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(bottomPart).with(bottomPartAlPha);
        animatorSet.play(topPart).with(topPartAlPha).after(1500);
        animatorSet.addListener(listener);
        animatorSet.start();
    }
}
