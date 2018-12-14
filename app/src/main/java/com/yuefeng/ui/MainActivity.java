package com.yuefeng.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.updateapputils.UpdateManager;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.LocationGpsUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.RxHelper;
import com.common.utils.TimeUtils;
import com.common.view.dialog.SigninCacheSureDialog;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.book.ui.fragment.AddressbookFragment;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.ui.fragment.FeaturesFragment;
import com.yuefeng.home.modle.NewMsgListDataBean;
import com.yuefeng.home.ui.adapter.ConversationListAdapterEx;
import com.yuefeng.home.ui.fragment.HomeFragment;
import com.yuefeng.login_splash.contract.SignInContract;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.login_splash.presenter.SignInPresenter;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.ui.base.fragment.NoSlideViewPager;
import com.yuefeng.ui.base.fragment.TabItemInfo;
import com.yuefeng.usercenter.ui.fragment.UserInfoFragment;
import com.yuefeng.utils.BdLocationUtil;
import com.yuefeng.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class MainActivity extends BaseActivity implements
        SignInContract.View, LocationUtils.OnResultMapListener {
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.ll_parent)
    LinearLayout ll_parent;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;

    @BindView(R.id.viewpager)
    NoSlideViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;
    private List<TabItemInfo> tabItemInfos;

    @BindString(R.string.tab_main_name)
    String msg_name;
    @BindString(R.string.tab_tack_name)
    String tack_name;
    @BindString(R.string.tab_search_name)
    String features_name;
    @BindString(R.string.tab_news_name)
    String book_name;
    @BindString(R.string.tab_mine_name)
    String my_name;

    private int mTime = 10;
    private SigninCacheSureDialog sureDialog;
    private static final int BDLOCATION_TIME = 10000;
    private boolean isFirstLocation = true;
    private SignInPresenter presenter;
    private String userId;
    private String address;
    private double latitude;
    private double longitude;

    private String AnnTime;
    private String ProjectTime;
    private String VersionTime;

    /*测试融云*/
    public static String libaiToken = "aHHKNr+jpANSk+uWQRY39gM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cCCo35Kr7Jm6letTWyBnuJKhz4P1/QEycI9EHoqNufxmiRbEGBi6ETk=";
    public static String dufuToken = "RYCau/8zuqnDOmQTWPRG1QM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cDJY62ny6gUtsHqBkVNTaVu99jIf/dRs6EZ/XPYM5WYb2aUb9QjRZ7s=";
    public static String wangxizhiToken = "DAH3ZJtjSLdz3oaJJPaMYgM6cY2j1V36sF/P4vhFDM0WqIiR2LA7cAQ4syW7Urv0jMLVuVYI85XcyUNRn2uYB2yGD+SXX0kF";
    public static String libaiName = "李白";
    public static String dufuName = "杜甫";
    public static String wangxizhiName = "王羲之";
    public static String libaiUserId = "zxcvbnmasdfghjkl";
    public static String dufuUserId = "asdfghjklzxcvbnm";
    public static String wangxizhiUserId = "qwertyuiop";
    private static String portraitUrl = "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            ButterKnife.bind(this);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            RongIMUtils.init(libaiUserId, libaiName, portraitUrl);
            RongIMUtils.connectToken(libaiToken);
            ll_parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            presenter = new SignInPresenter(this, this);
            initViewPager();
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            iv_back.setVisibility(View.INVISIBLE);
            tv_title.setText(msg_name);
            PreferencesUtils.putString(MyApplication.getContext(), "Fengrun", "无");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initData() {
        requestPermissions();
    }

    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        try {
            RxPermissions rxPermission = new RxPermissions(this);
            rxPermission.request(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
//                            if (!granted) {
//                                showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
//                            }
                            getLocation();
                            getdata();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {

        boolean gpsOPen = LocationGpsUtils.isGpsOPen(this);
        if (gpsOPen) {
            useBdGpsLocation();
        }
    }

    private void useBdGpsLocation() {
        try {
            BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
                @Override
                public void myLocation(BDLocation location) {
                    if (location == null) {
                        requestPermissions();
                        return;
                    }
                    address = location.getAddrStr();
                    if (isFirstLocation) {
                        if (!TextUtils.isEmpty(address) && address.contains(getString(R.string.CHINA))) {
                            int length = address.length();
                            address = address.substring(2, length);
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            isFirstLocation = false;
                            PreferencesUtils.putString(MainActivity.this, Constans.ADDRESS, address);
                            try {
                                String string = PreferencesUtils.getString(MainActivity.this, Constans.EMAIL, "");
                                if (string.equals("true")) {
                                    showSignInTime(longitude, latitude, address);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
//            }
            }, BDLOCATION_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getdata() {
        try {
            Observable.interval(0, 60, TimeUnit.SECONDS)
                    .compose(RxHelper.<Long>rxSchedulerHelper())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Long value) {
                            getNetDatas();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNetDatas() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(MainActivity.this, Constans.ORGID, "");
            presenter.getAnnouncementByuserid(ApiService.GETANNOUNCEMENTBYUSERID, pid, "", "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BdLocationUtil.getInstance().stopLocation();
    }

    /*倒计时*/
    private void showSignInTime(final double longitude, final double latitude, final String address) {
        try {
            PreferencesUtils.putBoolean(MainActivity.this, "isSignIn", true);
            if (sureDialog == null) {
                sureDialog = new SigninCacheSureDialog(this);
            }
            String time = TimeUtils.getCurrentTime();
            sureDialog.setTextTime(time);
            sureDialog.setTextAddress(address);
            sureDialog.setDeletaCacheListener(new SigninCacheSureDialog.DeletaCacheListener() {

                @Override
                public void sure() {
                    if (!isFinishing()) {
                        sureDialog.dismiss();
                    }
                    personalSignIn(longitude, latitude, address);
                    PreferencesUtils.putBoolean(MainActivity.this, "isSignIn", false);
                    checkVersion();

                }

                @Override
                public void cancle() {
                    if (!isFinishing()) {
                        sureDialog.dismiss();
                    }
                    PreferencesUtils.putBoolean(MainActivity.this, "isSignIn", false);
                    checkVersion();
                }
            });

            if (!isFinishing()) {
                sureDialog.show();
            }
            initCountDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCountDown() {
        try {
            Observable.interval(1, TimeUnit.SECONDS)
                    .take(mTime)//计时次数
                    .map(new Function<Long, Long>() {
                        @Override
                        public Long apply(Long aLong) throws Exception {
                            return mTime - aLong;// 3-0 3-2 3-1
                        }
                    })
                    .compose(RxHelper.<Long>rxSchedulerHelper())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Long value) {
                            //                        Logger.e("value = " + value);
                            String s = String.valueOf(value);
                            if (sureDialog != null)
                                sureDialog.setTextTimeDown(TextUtils.isEmpty(s) ? "" : "(" + s + " s)");
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {//结束，打卡
                            if (sureDialog != null && !isFinishing()) {
                                sureDialog.dismiss();
                            }
                            checkVersion();
                            boolean isSignIn = PreferencesUtils.getBoolean(MainActivity.this, "isSignIn");
                            if (isSignIn) {
                                personalSignIn(longitude, latitude, address);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Conversation.ConversationType[] mConversationsTypes = null;


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    private void initViewPager() {
        try {
            tabItemInfos = new ArrayList<>();
            tabItemInfos.add(new TabItemInfo(new HomeFragment(), R.drawable.home_button_selector, R.string.tab_main_name));

//        tabItemInfos.add(new TabItemInfo(new TackFragment(), R.drawable.search_button_selector, R.string.tab_tack_name));
            /*应用*/
            tabItemInfos.add(new TabItemInfo(new FeaturesFragment(), R.drawable.application_button_selector, R.string.tab_search_name));
            tabItemInfos.add(new TabItemInfo(new AddressbookFragment(), R.drawable.fuli_button_selector, R.string.tab_news_name));
            tabItemInfos.add(new TabItemInfo(new UserInfoFragment(), R.drawable.my_button_selector, R.string.tab_mine_name));
//            initConversationListUI();
            pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabItemInfos, mActivity);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(tabItemInfos.size());
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).select(); //默认选中某项放在加载viewpager之后
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                String titleName = "";

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    if (position == 0) {
                        titleName = msg_name;
                    } else if (position == 1) {
                        titleName = tack_name;
                    } else if (position == 2) {
                        titleName = features_name;
//                    } else if (position == 3) {
//                        titleName = book_name;
                    } else {
                        titleName = my_name;
                    }
                    tv_title.setText(titleName);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            initTabView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

    }

    private void personalSignIn(double longitude, double latitude, String address) {
        try {
            userId = PreferencesUtils.getString(this, Constans.ID, "");
            presenter.signIn(ApiService.QIANDAO, userId, "", "",
                    String.valueOf(longitude), String.valueOf(latitude), address, Constans.TYPE_ZERO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeSignInEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.LOGIN:
                break;
            case Constans.NEW_MSG_SUCCESS:
                List<NewMsgListDataBean> list = (List<NewMsgListDataBean>) event.getData();
                if (list.size() > 0) {
                    showAdapterDatasList(list);
                }
                break;
        }
    }

    private void initConversationListUI() {
        tabItemInfos.add(new TabItemInfo(initConversationList(), R.drawable.search_button_selector, R.string.tab_tack_name));
    }

    private void showAdapterDatasList(List<NewMsgListDataBean> list) {
        AnnTime = PreferencesUtils.getString(MainActivity.this, Constans.ANNTIME, "");
        ProjectTime = PreferencesUtils.getString(MainActivity.this, Constans.PROJECTTIME, "");
        VersionTime = PreferencesUtils.getString(MainActivity.this, Constans.VERSIONTIME, "");

        for (int i = 0; i < list.size(); i++) {
            String title = list.get(i).getSubject();
            String content = list.get(i).getContent();
            String time = list.get(i).getIssuedate();
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(content)) {
                if (i == 0) {
                    boolean startEndTime = TimeUtils.startSmortEndTime(AnnTime, time);
                    if (startEndTime) {
                        PreferencesUtils.putString(MainActivity.this, Constans.ANNTIME, time);
                        initNotification("[公告]" + list.get(i).getSubject(), list.get(i).getContent(), i);
                    }
                } else if (i == 1) {
                    boolean startEndTime = TimeUtils.startSmortEndTime(ProjectTime, time);
                    if (startEndTime) {
                        PreferencesUtils.putString(MainActivity.this, Constans.PROJECTTIME, time);
                        initNotification("[项目]" + list.get(i).getSubject(), list.get(i).getContent(), i);
                    }
                } else {
                    boolean startEndTime = TimeUtils.startSmortEndTime(VersionTime, time);
                    if (startEndTime) {
                        PreferencesUtils.putString(MainActivity.this, Constans.VERSIONTIME, time);
                        initNotification("[版本]" + list.get(i).getSubject(), list.get(i).getContent(), i);
                    }
                }
            }
        }
    }

    //    private void initNotification(String annoum, String project, String centent) {
    private void initNotification(String title, String content, int position) {

//        Notification.Builder builder = new Notification.Builder(AppUtils.getContext());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        NotificationManager mNotifyMgr = (NotificationManager) AppUtils.getContext().getSystemService(NOTIFICATION_SERVICE);
        Intent push = new Intent(AppUtils.getContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(AppUtils.getContext(), 0, push, FLAG_CANCEL_CURRENT);
        builder.setContentTitle(title);
        builder.setContentText("内容:" + content);   //内容
        builder.setWhen(System.currentTimeMillis());       //设置通知时间
        builder.setSmallIcon(R.mipmap.icon_app);            //设置小图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app));
        builder.setDefaults(Notification.DEFAULT_ALL);   //打开呼吸灯，声音，震动，触发系统默认行为
        builder.setAutoCancel(true);
        //设置点击后取消Notification
        //比较手机sdk版本与Android 5.0 Lollipop的sdk

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        String[] events = {annoum, project, centent};
//        inboxStyle.setBigContentTitle("侨银环保");
//        inboxStyle.setSummaryText("");
//        for (String event : events) {
//            inboxStyle.addLine(event);
//        }
//        builder.setStyle(inboxStyle);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            builder.setPriority(Notification.PRIORITY_DEFAULT);//设置该通知优先级
            builder.setCategory(Notification.CATEGORY_MESSAGE);//设置通知类别
        }
        builder.setContentIntent(contentIntent);

        //第五步：发送通知请求：
        Notification notify = builder.build();//得到一个Notification对象
        mNotifyMgr.notify(position, notify);//发送通知请求
    }

    @Override
    protected boolean isNeedTranslateBar() {
        return true;
    }

    //    检查版本更新
    private boolean HasCheckUpdate = false;
    private UpdateManager mUpdateManager;

    private void checkVersion() {
        if (!HasCheckUpdate) {
            mUpdateManager = new UpdateManager(MainActivity.this, false);
            mUpdateManager.checkVersion();
            HasCheckUpdate = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (mUpdateManager != null) {
                mUpdateManager.isAndoird8();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagerAdapter != null) {
            pagerAdapter.destroy();
            pagerAdapter = null;
        }
        tabItemInfos = null;
        tabLayout = null;
        EventBus.getDefault().unregister(this);
        RongIMUtils.destroyRongIM();
    }

    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        try {
            address = (String) map.get("address");
            if (!TextUtils.isEmpty(address)) {
                LatLng latLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(latitude, longitude));
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                showSignInTime(longitude, latitude, address);
            } else {
                useBdGpsLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGeoCodeResult(Map<String, Object> map) {

    }

}
