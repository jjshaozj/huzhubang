package com.a8plus1.seen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.Json.JUser;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;
    @InjectView(R.id.bt_signup)
    Button btGo;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.et_repeatpassword)
    EditText etRepeatPassword;

    String userID = "";
    String password = "";
    String passworda = "";
    SharedPreferences pref;//数据存储方式，轻量级
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //使用SharedPreferences
        pref = getSharedPreferences("Seen", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);//简化监听器方法

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//对SDK进行判断，判断成功后才能播放动画
            ShowEnterAnimation();//播放动画
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });//对FAB设置监听器,简化方式设置
    }

    private void ShowEnterAnimation() {//显示进入动画
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);//过渡动画效果
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);//持续时间
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {//关闭动画
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                SignupActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick(R.id.bt_signup)
    //***************************************************************************
    public void onClick(View view) {//设置触发点击事件
        userID = etUsername.getText().toString();
        password = etPassword.getText().toString();
        passworda = etRepeatPassword.getText().toString();
        //获取当前文本框内编辑的数据

        if (!(userID.equals("") || password.equals(""))) {//判空
            if (password.equals(passworda)) {//判断两次密码是否一致

                Request<String> request = new StringRequest(NetData.urlSignup, RequestMethod.POST);//创建请求对象，由NetData与RequestMethod内已写好的String进行访问
                JSONObject jsonObject = new JSONObject();//新建Json对象
                try {
                    //在这里面写请求内容，转化为json格式
                    jsonObject.put("userID", userID);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request.setDefineRequestBodyForJson(jsonObject);//将json正文存入request
                //开始通信
                RequestQueue queue = NoHttp.newRequestQueue();//创建一个请求队列
                queue.add(0, request, new OnResponseListener<String>() {//在请求队列内新建OnResponseListener对象,重写构造方法
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        if (response.responseCode() == 200) {//对服务器进行响应测试，对应答数据进行判断
                            if (JUser.getSuccess(response.get().toString())) {
                                Toast.makeText(getApplicationContext(), userID + "注册成功", Toast.LENGTH_SHORT).show();

                                UserInfo.userID = userID;//创建空信息
                                UserInfo.nickname = userID;//昵称
                                editor.putString("userID", UserInfo.userID);
                                editor.putString("nickname", UserInfo.nickname);
                                editor.putBoolean("loginSuccess", true);
                                editor.commit();

                                System.out.println("以下为注册用户信息");
                                System.out.println("userID:" + pref.getString("userID", ""));
                                System.out.println("nickname:" + pref.getString("nickname", ""));
                                System.out.println("loginSuccess:" + pref.getBoolean("loginSuccess", false));
                                System.out.println("tokenOK:" + pref.getBoolean("tokenOK", false));
                                //跳回主界面
                                Intent intent =new Intent(SignupActivity.this,MainActivity.class);//intent界面跳转
                                startActivity(intent);
                                finish();
                            } else {//注册失败
                                Toast.makeText(getApplicationContext(), "该用户名可能已被注册", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        Toast.makeText(getApplicationContext(), "注册超时！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStart(int what) {
                        System.out.println("[ProSS]正在注册");
                    }

                    @Override
                    public void onFinish(int what) {
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "输入的密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}