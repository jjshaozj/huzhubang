package com.a8plus1.seen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Adapter.MyFragmentViewPagerAdapter;
import com.a8plus1.seen.Bean.IMData;
import com.a8plus1.seen.Bean.MIFriend;
import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Json.JUser;
import com.a8plus1.seen.Utils.BitmapAndStringUtils;
import com.a8plus1.seen.Utils.GoIntent;
import com.a8plus1.seen.mainViewPagers.MainFragment;
import com.a8plus1.seen.mainViewPagers.MessageFragment;
import com.a8plus1.seen.mainViewPagers.SearchFragment;
import com.a8plus1.seen.mainViewPagers.IMlistFragment;
import com.a8plus1.seen.mainViewPagers.ZhenFaBuDeFragment;
import com.a8plus1.seen.mainViewPagers.ZhenZhunLeDeFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.a8plus1.seen.Bean.UserInfo;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {

    //本地XML
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //test Fragment ZhenFaBuDe;
    private FragmentManager manager;
    private FragmentTransaction ft;

    //我的信息
    private ImageView meImageView;
    //主页ViewPager
    public static ViewPager mainViewPager;
    //底部导航栏各按钮
    private RadioButton[] radioBtns;
    private RadioGroup radioGroup;
    //侧拉菜单以及其中控件
    private SlidingMenu slidingMenu;
    private ImageView headImage;
    private Button besend;
    private Button bedone;
    private Button setting;
    private TextView nickname;
    private TextView signature;//签名

    public static int currentPage = 0;


    //****MI
    private MIFriend currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//onCreate方法，随程序执行
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//主页面设置
        Toast.makeText(this, "正在加载…", Toast.LENGTH_SHORT).show();//弹出消息

        //初始化用户信息和融云链接
        initData();
        initMainView();

      /*  //此处为测试token
        IMData im = new IMData(this);
        im.getToken();
        */
    }

    private void initData() {
        //使用SharedPreferences
        pref = getSharedPreferences("Seen", MODE_PRIVATE); //获取的对象创建的文件
        editor = pref.edit();

        //打开APP时尝试本地读取个人信息，创建各项参数
        if (pref.getBoolean("loginSuccess", false)) {
            UserInfo.userID = pref.getString("userID", "");
            UserInfo.nickname = pref.getString("nickname", "");
            if (pref.getBoolean("tokenOK", false)) {
                UserInfo.token = pref.getString("token", "");
                UserInfo.signature = pref.getString("signature", "");
            }
            if (pref.getBoolean("imageExist", false)) {
                UserInfo.headImage = pref.getString("headImage", "");
            }
            currentUser = new MIFriend(UserInfo.userID, UserInfo.nickname, Uri.parse("https://free.modao.cc/uploads3/icons/623/6235610/retina_icon_1513177390.png"));
        }
//*************************************头像等未设置问题，初始化默认值 登陆问题
        if (TextUtils.isEmpty(UserInfo.nickname)&&TextUtils.isEmpty(UserInfo.userID)) {
                UserInfo.nickname = "互助帮";
        }
        if (TextUtils.isEmpty(UserInfo.signature)) {//判空
            UserInfo.signature ="帮助他人也是帮助自己";
        }
        if (TextUtils.isEmpty(UserInfo.headImage)) {
            Resources res = this.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.app_icon);  //activity中加载drawable资源的格式
            UserInfo.headImage = BitmapAndStringUtils.BitmapToString(bmp); //将bitmap转化为base64字符串

        }

        if (UserInfo.token.equals("")&&(!UserInfo.userID.equals(""))) {
            getToken2();   //有id却未被获取，获取用户信息
        } else if(!UserInfo.userID.equals("")){
            connectRongServer(UserInfo.token);
        }
    //后台输出检查信息
        System.out.println("以下为本地用户信息");
        System.out.println("userID:" + pref.getString("userID", ""));
        System.out.println("nickname:" + pref.getString("nickname", ""));
        System.out.println("loginSuccess:" + pref.getBoolean("loginSuccess", false));
        System.out.println("tokenOK:" + pref.getBoolean("tokenOK", false));
        System.out.println("imageExist:" + pref.getBoolean("imageExist", false));
        System.out.println("token:" + pref.getString("token", ""));
        if (!UserInfo.headImage.equals("")) {
            System.out.print("headImage:" + UserInfo.headImage.substring(UserInfo.headImage.length() - 20));
            System.out.println("头像所占空间:" + UserInfo.headImage.getBytes().length);
        }
    }

    private void initMainView() {//初始化页面

        //我的信息
        meImageView = findViewById(R.id.me_imageview_main);

        //主页面
        mainViewPager = findViewById(R.id.main_viewpager_main);
        initMainViewPager();

        //底部各按钮
        radioGroup = findViewById(R.id.maintab_radiogroup_main);
        radioBtns = new RadioButton[4];
        radioBtns[0] = findViewById(R.id.radio_message_main);
        radioBtns[1] = findViewById(R.id.radio_main_main);
        radioBtns[2] = findViewById(R.id.radio_search_main);
        radioBtns[3] = findViewById(R.id.radio_imlist_main);
        initMainBottomButton();//初始化按钮，设置按钮大小

        //设置侧拉菜单属性
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        //侧拉菜单控件
        headImage = findViewById(R.id.image_icon);
        besend = findViewById(R.id.besend);
        bedone = findViewById(R.id.bedone);
        setting = findViewById(R.id.setting);
        nickname = findViewById(R.id.name);
        signature = findViewById(R.id.signed);

        //**************调整到setView了
        String a = UserInfo.nickname;
        nickname.setText(UserInfo.nickname);
        signature.setText(UserInfo.signature);
        headImage.setImageBitmap(BitmapAndStringUtils.StringToBitmap(UserInfo.headImage));

        //初始化监听
        initMainListener();
    }

    private void initMainListener() {
        //设置侧拉菜单
        meImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingMenu.toggle(true);
            }
        });

        //侧滑菜单朕发布的控件跳转
        besend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfo.userID.equals(""))//True表示登录成功
                {
                    currentPage = 1;

                    Fragment fragment = new ZhenFaBuDeFragment();
                    manager = getSupportFragmentManager();
                    ft = manager.beginTransaction();
                    ft.replace(R.id.relativeLayout_main, fragment);
                    ft.addToBackStack("fr1");
                    ft.commit();
                    slidingMenu.toggle(false);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        //侧滑菜单朕准了的控件跳转
        bedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfo.userID.equals(""))//True表示登录成功
                {
                    currentPage = 1;

                    Fragment fragment = new ZhenZhunLeDeFragment();
                    manager = getSupportFragmentManager();
                    ft = manager.beginTransaction();
                    ft.replace(R.id.relativeLayout_main, fragment);
                    ft.addToBackStack("fr2");
                    ft.commit();
                    slidingMenu.toggle(false);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        // 侧滑菜单设置的控件跳转
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserInfo.userID.equals(""))//True表示登录成功
                {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        //ViewPager
        //页面转换监听器

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //获取当前页面用于改变对应RadioButton的状态
                int current = mainViewPager.getCurrentItem();
                switch (current) {
                    case 0:
                        radioBtns[0].setChecked(true);
                        break;
                    case 1:
                        radioBtns[1].setChecked(true);
                        break;
                    case 2:
                        radioBtns[2].setChecked(true);
                        break;
                    case 3:
                        radioBtns[3].setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Tab
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
                int current = 0;
                switch (i) {
                    case R.id.radio_message_main:
                        current = 0;
                        break;
                    case R.id.radio_main_main:
                        current = 1;
                        break;
                    case R.id.radio_search_main:
                        current = 2;
                        break;
                    case R.id.radio_imlist_main:
                        current = 3;
                        break;
                }
                if (mainViewPager.getCurrentItem() != current) {
                    mainViewPager.setCurrentItem(current);
                }
            }
        });

    }

    private void initMainViewPager() {
        //初始化主视图
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new MainFragment());
        fragments.add(new SearchFragment());
        fragments.add(new IMlistFragment());

        MessageRecyclerAdapter.messageFragment = (MessageFragment) fragments.get(0);
        MessageRecyclerAdapter.mainFragment = (MainFragment) fragments.get(1);

        mainViewPager.setAdapter(new MyFragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
        mainViewPager.setCurrentItem(0);
        mainViewPager.setOffscreenPageLimit(3);
    }

    private void initMainBottomButton() {
        for (int i = 0; i < radioBtns.length; i++) {
            //创建新的draid数组，编辑按钮大小
            Drawable[] draid = radioBtns[i].getCompoundDrawables();

            //setBounds (1,2,3,4) 1是距左右边距离,2是距上下边距离,3是长度,4是宽度i
            draid[1].setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.tab_radio_height),
                    getResources().getDimensionPixelSize(R.dimen.tab_radio_width));
            radioBtns[i].setCompoundDrawables(draid[0], draid[1], draid[2], draid[3]);
        }
    }

    //二级退出
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentPage == 1) {
                //如果是主页面 就不可往左滑动
                currentPage = 0;
                onBackPressed();
                return false;
            }
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        //
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.showContent();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }


    private void connectRongServer(String token) {
        //如果token为空 就别连了
        if (token.equals("")) return;
String SSS = token;
//连接融云
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onSuccess(String s) {
                if (s.equals(currentUser.getUserId()))
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setCurrentUserInfo(currentUser);
                    }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("rong123","errcode");
            }

            //****************************************这里上线前必写 先留存
            @Override
            public void onTokenIncorrect() {
                Log.d("rong123", "--onTokenIncorrect");
                getToken2();
            }
        });
    }

    private void getToken2() {//获取令牌
        System.out.println("[ProSS]获取" + UserInfo.userID + "的个人信息");
        Request<String> request = new StringRequest(NetData.urlGetInfo, RequestMethod.POST);
        JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            jsonObject.put("userID", UserInfo.userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(jsonObject);

        //开始通信
        RequestQueue queue = NoHttp.newRequestQueue();//创建一个请求队列
//        Logger.setDebug(true); // 开启NoHttp调试模式。
//        Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。
        queue.add(0, request, new OnResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (response.responseCode() == 200) {

                    System.out.println("[ProSS]用户信息存入UserInfo");
                    JUser.getUserInfo(response.get().toString());

                    editor.putString("userID", UserInfo.userID);
                    editor.putString("nickname", UserInfo.nickname);
                    editor.putString("signature", UserInfo.signature);
                    editor.putString("token", UserInfo.token);

                    if (!UserInfo.userID.equals(""))
                        editor.putBoolean("loginSuccess", true);

                    if (!UserInfo.nickname.equals(""))
                        editor.putBoolean("infoOK", true);

                    if (!UserInfo.headImage.equals(""))
                        editor.putBoolean("imageExist", true);

                    editor.commit();
                    System.out.println("以下为用户信息");
                    System.out.println("userID:" + pref.getString("userID", ""));
                    System.out.println("nickname:" + pref.getString("nickname", ""));
                    System.out.println("loginSuccess:" + pref.getBoolean("loginSuccess", false));
                    System.out.println("infoOK:" + pref.getBoolean("infoOK", false));
                    System.out.println("imageExist:" + pref.getBoolean("imageExist", false));
                    System.out.println("token:" + pref.getString("token", ""));
                    if (!UserInfo.headImage.equals("")) {
                        System.out.print("headImage:" + UserInfo.headImage.substring(UserInfo.headImage.length() - 20));
                        System.out.println("头像所占空间:" + UserInfo.headImage.getBytes().length);
                    }
                    connectRongServer(UserInfo.token);
                } else {
                    System.out.println("[ProSS]服务器响应失败");
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                System.out.println("[ProSS]获取信息超时！");
            }

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
