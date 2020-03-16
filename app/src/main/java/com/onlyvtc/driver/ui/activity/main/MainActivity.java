package com.onlyvtc.driver.ui.activity.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseActivity;
import com.onlyvtc.driver.common.ChatHeadService;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.common.GPSTracker;
import com.onlyvtc.driver.common.LocaleHelper;
import com.onlyvtc.driver.common.PolyUtil;
import com.onlyvtc.driver.common.SharedHelper;
import com.onlyvtc.driver.common.Utilities;
import com.onlyvtc.driver.common.chat.ChatActivity;
import com.onlyvtc.driver.common.fcm.MyFireBaseMessagingService;
import com.onlyvtc.driver.common.swipe_button.SwipeButton;
import com.onlyvtc.driver.data.network.model.Advertisement;
import com.onlyvtc.driver.data.network.model.LatLngFireBaseDB;
import com.onlyvtc.driver.data.network.model.SettingsResponse;
import com.onlyvtc.driver.data.network.model.TripResponse;
import com.onlyvtc.driver.data.network.model.UserResponse;
import com.onlyvtc.driver.data.network.model.WayPoint;
import com.onlyvtc.driver.ui.activity.add_card.AddCardActivity;
import com.onlyvtc.driver.ui.activity.card.CardActivity;
import com.onlyvtc.driver.ui.activity.currentride.CurrentRideActivity;
import com.onlyvtc.driver.ui.activity.document.DocumentActivity;
import com.onlyvtc.driver.ui.activity.earnings.EarningsActivity;
import com.onlyvtc.driver.ui.activity.help.HelpActivity;
import com.onlyvtc.driver.ui.activity.instant_ride.InstantRideActivity;
import com.onlyvtc.driver.ui.activity.invite.InviteActivity;
import com.onlyvtc.driver.ui.activity.invite_friend.InviteFriendActivity;
import com.onlyvtc.driver.ui.activity.notification_manager.NotificationManagerActivity;
import com.onlyvtc.driver.ui.activity.profile.ProfileActivity;
import com.onlyvtc.driver.ui.activity.setting.SettingsActivity;
import com.onlyvtc.driver.ui.activity.summary.SummaryActivity;
import com.onlyvtc.driver.ui.activity.wallet.WalletActivity;
import com.onlyvtc.driver.ui.activity.your_trips.YourTripActivity;
import com.onlyvtc.driver.ui.adapter.AdsAdapter;
import com.onlyvtc.driver.ui.bottomsheetdialog.invoice_flow.InvoiceDialogFragment;
import com.onlyvtc.driver.ui.bottomsheetdialog.rating.RatingDialogFragment;
import com.onlyvtc.driver.ui.fragment.incoming_request.IncomingRequestFragment;
import com.onlyvtc.driver.ui.fragment.offline.OfflineFragment;
import com.onlyvtc.driver.ui.fragment.status_flow.StatusFlowFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onlyvtc.driver.MvpApplication.DATUM;
import static com.onlyvtc.driver.MvpApplication.DEFAULT_ZOOM;
import static com.onlyvtc.driver.MvpApplication.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.onlyvtc.driver.MvpApplication.canGoToChatScreen;
import static com.onlyvtc.driver.MvpApplication.isChatScreenOpen;
import static com.onlyvtc.driver.MvpApplication.mLastKnownLocation;
import static com.onlyvtc.driver.common.Constants.checkStatus.ARRIVED;
import static com.onlyvtc.driver.common.Constants.checkStatus.MIDSTOPPED;
import static com.onlyvtc.driver.common.Constants.checkStatus.PICKEDUP;
import static com.onlyvtc.driver.common.Constants.checkStatus.STARTED;

public class MainActivity extends BaseActivity implements
        MainIView,
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        DirectionCallback {

    private static final int APP_PERMISSION_REQUEST = 102;
    //    private static final int CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 104;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.lnrLocationHeader)
    LinearLayout lnrLocationHeader;
    @BindView(R.id.lblLocationType)
    TextView lblLocationType;
    @BindView(R.id.lblLocationName)
    TextView lblLocationName;
    @BindView(R.id.offline_container)
    FrameLayout offlineContainer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.gps)
    ImageView gps;
    @BindView(R.id.navigation_img)
    ImageView navigationImg;
    @BindView(R.id.sbChangeStatus)
    SwipeButton sbChangeStatus;
    TextView lblMenuName, lblMenuEmail, lblMenuWallet;
    ImageView imgMenu, imgStatus;
    MainPresenter presenter = new MainPresenter();
    SupportMapFragment mapFragment;
    GoogleMap googleMap;

    private Runnable r;
    private Handler h;
    private int delay = 5000;
    private String STATUS = "";
    private String CURRENT_DEST_ADDRESS = "";
    private String ACCOUNTSTATUS = "";
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocation;
    private BottomSheetBehavior bottomSheetBehavior;
    private Intent gpsServiceIntent;
    //    private Intent floatingWidgetIntent;
    private DatabaseReference mProviderLocation;
    private ArrayList<LatLng> polyLinePoints;
    private Polyline mPolyline;
    private boolean canReRoute = true, canCarAnim = true;
    private LatLng start = null, end = null;
    private PopupWindow popupwindow_obj;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap<String, Object> params = new HashMap<>();
            if (mLastKnownLocation != null) {
                params.put("latitude", mLastKnownLocation.getLatitude());
                params.put("longitude", mLastKnownLocation.getLongitude());
            }
            presenter.getTrip(params);
        }
    };
    private int canMapAnimate;
    private Marker srcMarker;

    private LoopRecyclerViewPager recyclerView_Ads;
    private List<Advertisement> m_aryAds;
    private AdsAdapter m_adapter;


    private static void startFloatingViewService(Activity activity) {
        // *** You must follow these rules when obtain the cutout(FloatingViewManager.findCutoutSafeArea) ***
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 1. 'windowLayoutInDisplayCutoutMode' do not be set to 'never'
            if (activity.getWindow().getAttributes().layoutInDisplayCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER) {
                throw new RuntimeException("'windowLayoutInDisplayCutoutMode' do not be set to 'never'");
            }
            // 2. Do not set Activity to landscape
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                throw new RuntimeException(activity.getString(R.string.do_not_set_activity_landscape));
            }
        }

        final Class<? extends Service> service;
        service = ChatHeadService.class;

        final Intent intent = new Intent(activity, service);
        //intent.putExtra(ChatHeadService.EXTRA_CUTOUT_SAFE_AREA, FloatingViewManager.findCutoutSafeArea(activity));
        ContextCompat.startForegroundService(activity, intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        registerReceiver(myReceiver, new IntentFilter(MyFireBaseMessagingService.INTENT_FILTER));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        imgMenu = headerView.findViewById(R.id.imgMenu);
        lblMenuName = headerView.findViewById(R.id.lblMenuName);
        lblMenuWallet = headerView.findViewById(R.id.lblMenuWallet);

        imgStatus = headerView.findViewById(R.id.imgStatus);
        lblMenuEmail = headerView.findViewById(R.id.lblMenuEmail);
        recyclerView_Ads = headerView.findViewById(R.id.recycle_advertisement);
        initAdvertisement();

        headerView.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                    (this, imgMenu, ViewCompat.getTransitionName(imgMenu));
            startActivity(new Intent(MainActivity.this, ProfileActivity.class), options.toBundle());
        });

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheetBehavior = BottomSheetBehavior.from(container);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Un used
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Un used
            }
        });

        String profileUrl = getIntent().getStringExtra("avartar");
        if (profileUrl != null && !profileUrl.isEmpty())
            Glide.with(activity())
                    .load(profileUrl)
                    .apply(RequestOptions
                            .placeholderOf(R.drawable.ic_user_placeholder)
                            .dontAnimate()
                            .error(R.drawable.ic_user_placeholder))
                    .into(imgMenu);

        sbChangeStatus.setOnStateChangeListener(active -> {
            if (active) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("service_status", "offline");
                presenter.providerAvailable(map);
            }
        });

        /*if (floatingWidgetIntent == null)
            floatingWidgetIntent = new Intent(MainActivity.this, FloatWidgetService.class);*/

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                presenter.getSettings();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        showFloatingView(activity(), true);
        popupwindow_obj = popupDisplay();

    }

    @SuppressLint("NewApi")
    private void showFloatingView(Context context, boolean isShowOverlayPermission) {
        // API22以下かチェック
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startFloatingViewService(activity());
            return;
        }

        if (Settings.canDrawOverlays(context)) {
            startFloatingViewService(activity());
            return;
        }

        if (isShowOverlayPermission) {
//            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
//            startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    private void startCheckStatusCall() {
        try {
            h = new Handler();
            r = () -> {
                if (mLastKnownLocation != null) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("latitude", mLastKnownLocation.getLatitude());
                    params.put("longitude", mLastKnownLocation.getLongitude());
                    presenter.getTrip(params);
                } else if (DATUM != null && DATUM.getStatus() != null && (DATUM.getStatus().equals(STARTED) || DATUM.getStatus().equals(ARRIVED)
                        || DATUM.getStatus().equals(PICKEDUP) || DATUM.getStatus().equals(MIDSTOPPED)))
                    if (canMapAnimate % 3 == 0) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polyLinePoints) builder.include(latLng);
                        LatLngBounds bounds = builder.build();
                        try {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250));
                        } catch (Exception e) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
                        }
                    }
                canMapAnimate++;
                h.postDelayed(r, delay);
            };
            h.postDelayed(r, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_PERMISSION_REQUEST && resultCode == RESULT_OK) openMap();
        else
            Toast.makeText(this, getString(R.string.draw_permission_not_enable), Toast.LENGTH_SHORT).show();

//        if (requestCode == CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE) {
//            showFloatingView(activity(), false);
//        }
    }

    public PopupWindow popupDisplay() { // disply designing your popoup window
        final PopupWindow popupWindow = new PopupWindow(this); // inflet your layout or diynamic add view

        View view;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.nav_menu_layout, null);

        ImageView imvWazeNav = (ImageView) view.findViewById(R.id.imvWazeNav);
        imvWazeNav.setOnClickListener(onClickNav);
        ImageView imvGMapNav = (ImageView) view.findViewById(R.id.imvGMapNav);
        imvGMapNav.setOnClickListener(onClickNav);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private View.OnClickListener onClickNav = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imvWazeNav:
                    openWaze();
                    break;
                case R.id.imvGMapNav:
                    openMap();
                    break;
            }
        }
    };

    private void openMap() {
        /*try {
            startService(floatingWidgetIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (getString(R.string.pick_up_location).equalsIgnoreCase(lblLocationType.getText().toString())) {
            if (DATUM.getSAddress() != null && !DATUM.getSAddress().isEmpty()) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + DATUM.getSAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        } else if (DATUM.getDAddress() != null && !DATUM.getDAddress().isEmpty()) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + DATUM.getDAddress());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void openWaze() {
        String address = "";
        if (getString(R.string.pick_up_location).equalsIgnoreCase(lblLocationType.getText().toString())) {
            address = DATUM.getSAddress();
        } else if (DATUM.getDAddress() != null && !DATUM.getDAddress().isEmpty()) {
            address = DATUM.getDAddress();
        }

        Uri naviUri3 = Uri.parse("geo:0,0?q=" + address);
        Intent intentWaze = new Intent(Intent.ACTION_VIEW, naviUri3);
        if (isPackageInstalled("com.waze", getPackageManager())) {
            intentWaze.setPackage("com.waze");
            startActivity(intentWaze);
        } else {
            Toast.makeText(this, getString(R.string.waze_not_installed), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ACCOUNTSTATUS = "";
        STATUS = "";
        // gpsServiceIntent = new Intent(this, GPSTracker.class);
        //startService(gpsServiceIntent);
        presenter.getProfile();
        HashMap<String, Object> params = new HashMap<>();
        if (mLastKnownLocation != null) {
            params.put("latitude", mLastKnownLocation.getLatitude());
            params.put("longitude", mLastKnownLocation.getLongitude());
            SharedHelper.putKey(this, Constants.SharedPref.LATITUDE, String.valueOf(mLastKnownLocation.getLatitude()));
            SharedHelper.putKey(this, Constants.SharedPref.LONGITUDE, String.valueOf(mLastKnownLocation.getLongitude()));
        }
        presenter.getTrip(params);
        registerReceiver(myReceiver, new IntentFilter(MyFireBaseMessagingService.INTENT_FILTER));
        startCheckStatusCall();

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
        h.removeCallbacks(r);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (floatingWidgetIntent != null)
            stopService(floatingWidgetIntent);*/
        // if (gpsServiceIntent != null) stopService(gpsServiceIntent);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) activity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GPSTracker.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_card:
                startActivity(new Intent(this, CardActivity.class));
                break;
            case R.id.nav_instant_ride:
                startActivity(new Intent(this, InstantRideActivity.class));
                break;
            case R.id.nav_your_trips:
                startActivity(new Intent(this, YourTripActivity.class));
                break;
            case R.id.nav_earnings:
                startActivity(new Intent(this, EarningsActivity.class));
                break;
            case R.id.nav_wallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.nav_summary:
                startActivity(new Intent(this, SummaryActivity.class));
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("setting", "isClick");
                startActivity(intent);
                break;
            case R.id.nav_notification:
                startActivity(new Intent(this, NotificationManagerActivity.class));
                break;
            case R.id.nav_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nav_share:
                navigateToShareScreen();
                break;
            case R.id.nav_document:
                startActivity(new Intent(this, DocumentActivity.class));
                break;
            case R.id.nav_invite_referral:
                startActivity(new Intent(this, InviteActivity.class));
                break;
            case R.id.nav_invite_friends:
                startActivity(new Intent(this, InviteFriendActivity.class));
                break;
            case R.id.nav_current_ride:
                startActivity(new Intent(this, CurrentRideActivity.class));
                break;
            case R.id.nav_logout:
                ShowLogoutPopUp();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onCameraIdle() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onCameraMove() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        } catch (Resources.NotFoundException e) {
            Log.d("Map:Style", "Can't find style. Error: ");
        }
        this.googleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        boolean serviceRunningStatus = isServiceRunning();

        if (serviceRunningStatus) {
            Intent serviceIntent = new Intent(this, GPSTracker.class);
            stopService(serviceIntent);
        }
        if (!serviceRunningStatus) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                activity().startService(new Intent(activity(), GPSTracker.class));
            } else {
                Intent serviceIntent = new Intent(activity(), GPSTracker.class);
                ContextCompat.startForegroundService(activity(), serviceIntent);
            }
        }
    }

    @OnClick({R.id.menu, R.id.nav_view, R.id.navigation_img, R.id.gps})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu:

                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else {
                    UserResponse user = new Gson().fromJson(SharedHelper.getKey(this, "userInfo"), UserResponse.class);
                    if (user != null) {
                        SharedHelper.putKey(this, Constants.SharedPref.CURRENCY, user.getCurrency());
                        Constants.Currency = SharedHelper.getKey(this, Constants.SharedPref.CURRENCY);

                        lblMenuName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                        lblMenuEmail.setText(user.getEmail());
                        lblMenuWallet.setText(MvpApplication.getInstance().getNewNumberFormat(user.getWalletBalance()));
                        lblMenuWallet.setTextColor(user.getWalletBalance() < 0 ? Color.RED : Color.BLACK);

                        SharedHelper.putKey(activity(), Constants.SharedPref.PICTURE, user.getAvatar());
                        Glide.with(activity())
                                .load(BuildConfig.BASE_IMAGE_URL + user.getAvatar())
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(imgMenu);
                    } else presenter.getProfile();
                    drawerLayout.openDrawer(Gravity.START);
                }

                break;
            case R.id.nav_view:
                break;
            case R.id.gps:
                if (mLastKnownLocation != null) {
                    LatLng currentLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                }
                break;
            case R.id.navigation_img:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName())), APP_PERMISSION_REQUEST);
                else {
                    popupwindow_obj.showAsDropDown(findViewById(R.id.navigation_img), -40, 22);
                }
//                else openMap();
                break;
        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) mLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void updateLocationUI() {
        if (googleMap == null) return;
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.setOnCameraMoveListener(this);
                googleMap.setOnCameraIdleListener(this);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocation.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Set the map's camera position to the current location of the device.
                        Log.d("getDeviceLocation", "Set the map's camera position to the current location of the device.");
                        mLastKnownLocation = task.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                        mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()),
                                DEFAULT_ZOOM));
                    } else {
                        Log.e("Map", "Current location is null. Using defaults.: %s", task.getException());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                    getDeviceLocation();
                }
            }
        }
    }

    private void changeFragment(Fragment fragment) {

        if (isFinishing()) return;

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, fragment.getTag());
            transaction.commitAllowingStateLoss();
            sbChangeStatus.setVisibility(View.GONE);
        } else {
            if (IncomingRequestFragment.countDownTimer != null) {
                IncomingRequestFragment.countDownTimer.cancel();
                if (IncomingRequestFragment.mPlayer.isPlaying())
                    IncomingRequestFragment.mPlayer.stop();
            }
            container.removeAllViews();
            sbChangeStatus.collapseButton();
            sbChangeStatus.setVisibility(View.VISIBLE);
            lnrLocationHeader.setVisibility(View.GONE);
            googleMap.clear();
        }
    }

    private void offlineFragment(String s) {
        Fragment fragment = new OfflineFragment();
        Bundle b = new Bundle();
        b.putString("status", s);
        fragment.setArguments(b);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.offline_container, fragment, fragment.getTag());
        transaction.commitAllowingStateLoss();
        ACCOUNTSTATUS = "";
    }

    @Override
    public void onSuccess(SettingsResponse response) {
        if (response.getReferral().getReferral().equalsIgnoreCase("1")) navMenuVisibility(true);
        else navMenuVisibility(false);
    }

    @Override
    public void onSettingError(Throwable e) {
        navMenuVisibility(false);
    }

    private void navMenuVisibility(boolean visibility) {
        Menu nav_Menu = navView.getMenu();
        MenuItem nav_invite_friend = nav_Menu.findItem(R.id.nav_invite_friends);
        nav_invite_friend.setVisible(visibility);
    }

    @Override
    public void onSuccess(UserResponse user) {
        if (user != null) {
            String dd = LocaleHelper.getLanguage(this);
            if (user.getProfile() != null && user.getProfile().getLanguage() != null &&
                    !user.getProfile().getLanguage().equalsIgnoreCase(dd)) {
                LocaleHelper.setLocale(getApplicationContext(), user.getProfile().getLanguage());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            lblMenuName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            lblMenuEmail.setText(user.getEmail());
            lblMenuWallet.setText(MvpApplication.getInstance().getNewNumberFormat(user.getWalletBalance()));
            lblMenuWallet.setTextColor(user.getWalletBalance() < 0 ? Color.RED : Color.BLACK);

            SharedHelper.putKey(activity(), Constants.SharedPref.PICTURE, user.getAvatar());
            Glide.with(activity())
                    .load(BuildConfig.BASE_IMAGE_URL + user.getAvatar())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                            .dontAnimate()
                            .error(R.drawable.ic_user_placeholder))
                    .into(imgMenu);
            SharedHelper.putKey(this, Constants.SharedPref.STRIPE_PUBLISHABLE_KEY, user.getStripePublishableKey());
            SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf(user.getId()));
            SharedHelper.putKey(this, Constants.SharedPref.USER_NAME, user.getFirstName()
                    + " " + user.getLastName());
            SharedHelper.putKey(this, Constants.SharedPref.USER_AVATAR, BuildConfig.BASE_IMAGE_URL + user.getAvatar());
            SharedHelper.putKey(this, Constants.SharedPref.CURRENCY, user.getCurrency());
            SharedHelper.putKey(this, Constants.SharedPref.SERVICE_TYPE, user.getService().getServiceType().getId());
            SharedHelper.putKey(this, Constants.SharedPref.USER_INFO, printJSON(user));
            Constants.Currency = SharedHelper.getKey(this, Constants.SharedPref.CURRENCY);
            int card = user.getCard();
            if (card == 0) {
                Menu nav_Menu = navView.getMenu();
                nav_Menu.findItem(R.id.nav_card).setVisible(false);
            } else {
                Menu nav_Menu = navView.getMenu();
                nav_Menu.findItem(R.id.nav_card).setVisible(true);
            }
            SharedHelper.putKey(this, "card", card);

            SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_CODE, user.getReferral_unique_id());
            SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_COUNT, user.getReferral_count());
            SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_TEXT, user.getReferral_text());
            SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_TOTAL_TEXT, user.getReferral_total_text());
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public void onSuccessLogout(Object object) {
        Utilities.LogoutApp(activity(), "");
    }

    @Override
    public void onSuccess(TripResponse response) {
        String accountStatus = response.getAccountStatus();
        String serviceStatus = response.getServiceStatus();

        MvpApplication.tripResponse = response;

        if (!ACCOUNTSTATUS.equalsIgnoreCase(accountStatus)) {
            ACCOUNTSTATUS = accountStatus;
            if (accountStatus.equalsIgnoreCase(Constants.User.Account.PENDING_DOCUMENT)) {
                startActivity(new Intent(MainActivity.this, DocumentActivity.class));
                imgStatus.setImageResource(R.drawable.banner_waiting);
            } else if (accountStatus.equalsIgnoreCase(Constants.User.Account.PENDING_CARD)) {
                startActivity(new Intent(MainActivity.this, AddCardActivity.class));
                imgStatus.setImageResource(R.drawable.banner_waiting);
            } else if (accountStatus.equalsIgnoreCase(Constants.User.Account.ONBOARDING)) {
                offlineFragment(Constants.User.Account.ONBOARDING);
                imgStatus.setImageResource(R.drawable.banner_waiting);
            } else if (Constants.User.Account.BANNED.equalsIgnoreCase(accountStatus)) {
                offlineFragment(Constants.User.Account.BANNED);
                imgStatus.setImageResource(R.drawable.banner_banned);
            } else if (Constants.User.Account.APPROVED.equalsIgnoreCase(accountStatus)
                    && Constants.User.Service.OFFLINE.equalsIgnoreCase(serviceStatus)) {
                offlineFragment(Constants.User.Service.OFFLINE);
                imgStatus.setImageResource(R.drawable.banner_active);
            } else if (Constants.User.Account.APPROVED.equalsIgnoreCase(accountStatus)
                    && Constants.User.Service.ACTIVE.equalsIgnoreCase(serviceStatus)) {
                offlineContainer.removeAllViews();
                imgStatus.setImageResource(R.drawable.banner_active);
            } else if (Constants.User.Account.BALANCE.equalsIgnoreCase(accountStatus)
                    || Constants.User.Service.BALANCE.equalsIgnoreCase(serviceStatus)) {
                offlineFragment(Constants.User.Service.BALANCE);
                imgStatus.setImageResource(R.drawable.banner_active);
            }
        }

        if (response.getRequests().isEmpty()) {
            CURRENT_DEST_ADDRESS = "";
            googleMap.clear();
            getDeviceLocation();
            changeFlow(Constants.checkStatus.EMPTY);
        } else {
            MvpApplication.time_to_left = response.getRequests().get(0).getTimeLeftToRespond();
            DATUM = response.getRequests().get(0).getRequest();
            changeFlow(DATUM.getStatus());
        }

        if (canGoToChatScreen) {
            if (!isChatScreenOpen && DATUM != null) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra(Constants.SharedPref.REQUEST_ID, String.valueOf(DATUM.getId()));
                startActivity(i);
            }
            canGoToChatScreen = false;
        }

    }

    @Override
    public void onSuccessProviderAvailable(Object object) {
        offlineFragment("");
        sbChangeStatus.toggleState();
    }

    @Override
    public void onSuccessFCM(Object object) {
        Utilities.printV("onSuccessFCM", "onSuccessFCM");
    }

    @Override
    public void onSuccessLocationUpdate(TripResponse tripResponse) {

    }

    @SuppressLint("StringFormatInvalid")
    public void changeFlow(String status) {

        System.out.println("RRR status = [" + status + "]");

        if (!DATUM.getDAddress().equalsIgnoreCase(CURRENT_DEST_ADDRESS)
                && STATUS.equalsIgnoreCase(Constants.checkStatus.PICKEDUP)
                && status.equalsIgnoreCase(Constants.checkStatus.PICKEDUP))
            showDestinationAlert(String.format(getString(R.string.destination_change_to), DATUM.getDAddress()));

        System.out.println("RRR getProviderId = " + "loc_p_" + DATUM.getProviderId());

        switch (status) {
            case Constants.checkStatus.ACCEPTED:
            case Constants.checkStatus.STARTED:
            case Constants.checkStatus.ARRIVED:
            case Constants.checkStatus.PICKEDUP:
            case Constants.checkStatus.MIDSTOPPED:
                String refPath = "loc_p_" + DATUM.getProviderId();
                if (!refPath.equals("loc_p_0") && mProviderLocation == null) {
                    mProviderLocation = FirebaseDatabase.getInstance().getReference()
                            .child("loc_p_" + DATUM.getProviderId());
                    updateDriverNavigation();
                }
                break;
            case Constants.checkStatus.DROPPED:
            case Constants.checkStatus.COMPLETED:
            default:
                mProviderLocation = null;
                break;
        }

        if (!STATUS.equalsIgnoreCase(status)) {
            STATUS = status;
            CURRENT_DEST_ADDRESS = DATUM.getDAddress();
            lblLocationType.setText(getString(R.string.pick_up_location));
            if (DATUM != null && DATUM.getSAddress() != null && !DATUM.getSAddress().isEmpty())
                lblLocationName.setText(DATUM.getSAddress());
            CURRENT_DEST_ADDRESS = DATUM.getDAddress();
            switch (status) {
                case Constants.checkStatus.EMPTY:
                    mProviderLocation = null;
                    changeFragment(null);
                    break;
                case Constants.checkStatus.SEARCHING:
                    changeFragment(new IncomingRequestFragment());
                    break;
                case Constants.checkStatus.ACCEPTED:
                case Constants.checkStatus.STARTED:
                    lnrLocationHeader.setVisibility(View.VISIBLE);
                    changeFragment(new StatusFlowFragment());
                    break;
                case Constants.checkStatus.ARRIVED:
                    googleMap.clear();
                    lblLocationType.setText(getString(R.string.pick_up_location));
                    lblLocationName.setText(DATUM.getSAddress());
                    lnrLocationHeader.setVisibility(View.VISIBLE);
                    changeFragment(new StatusFlowFragment());
                    break;
                case Constants.checkStatus.PICKEDUP:
                    // check stop location
                    // if there is stop location then show stop location else dropoff location
                    WayPoint waypoint = null;
                    String wayPointString = DATUM.getWay_points();
                    try {
                        JSONArray ary = new JSONArray(wayPointString);
                        if (ary.length() > 0) {
                            waypoint = new WayPoint(ary.optJSONObject(0));
                        }
                    } catch (Exception e) {
                        Log.d("tag", e.toString());
                    }
                    if (waypoint != null) {
                        lblLocationType.setText(getString(R.string.stop_location));
                        lblLocationName.setText(waypoint.getAddress());
                    } else {
                        lblLocationType.setText(getString(R.string.drop_off_location));
                        lblLocationName.setText(DATUM.getDAddress());
                    }
                    lnrLocationHeader.setVisibility(View.VISIBLE);
                    changeFragment(new StatusFlowFragment());
                    break;
                case Constants.checkStatus.MIDSTOPPED:
                    lblLocationType.setText(getString(R.string.drop_off_location));
                    lblLocationName.setText(DATUM.getDAddress());
                    lnrLocationHeader.setVisibility(View.VISIBLE);
                    changeFragment(new StatusFlowFragment());
                    break;
                case Constants.checkStatus.DROPPED:
                    lblLocationType.setText(getString(R.string.drop_off_location));
                    lblLocationName.setText(DATUM.getDAddress());
                    changeFragment(new InvoiceDialogFragment());
                    break;
                case Constants.checkStatus.COMPLETED:
                    lblLocationType.setText(getString(R.string.drop_off_location));
                    lblLocationName.setText(DATUM.getDAddress());
                    changeFragment(new RatingDialogFragment());
                    break;
                default:
                    break;
            }
        } else System.out.println("Opened Dialogs ==> " + hasOpenedDialogs());
    }

    private void updateDriverNavigation() {
        mProviderLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    LatLngFireBaseDB fbData = dataSnapshot.getValue(LatLngFireBaseDB.class);
                    assert fbData != null;
                    double lat = fbData.getLat();
                    double lng = fbData.getLng();

                    System.out.println("RRR Lat: " + lat + " Lng: " + lng + "--" + DATUM.getStatus());
                    if (lng > 0 && lat > 0)
                        if (STARTED.equalsIgnoreCase(DATUM.getStatus()) ||
                                ARRIVED.equalsIgnoreCase(DATUM.getStatus()) ||
                                PICKEDUP.equalsIgnoreCase(DATUM.getStatus()) ||
                                MIDSTOPPED.equalsIgnoreCase(DATUM.getStatus())) {
                            LatLng source = null, destination = null;
                            switch (DATUM.getStatus()) {
                                case STARTED:
                                case ARRIVED:
                                    source = new LatLng(lat, lng);
                                    destination = new LatLng(DATUM.getSLatitude(), DATUM.getSLongitude());
                                    break;
                                case PICKEDUP:
                                    WayPoint waypoint = null;
                                    String wayPointString = DATUM.getWay_points();
                                    try {
                                        JSONArray ary = new JSONArray(wayPointString);
                                        if (ary.length() > 0) {
                                            waypoint = new WayPoint(ary.optJSONObject(0));
                                        }
                                    } catch (Exception e) {
                                        Log.d("tag", e.toString());
                                    }
                                    if (waypoint != null) {
                                        source = new LatLng(lat, lng);
                                        destination = new LatLng(waypoint.getLat(), waypoint.getLng());
                                        Log.d("PICKEDUP--", "stopped");
                                    } else {
                                        source = new LatLng(lat, lng);
                                        destination = new LatLng(DATUM.getDLatitude(), DATUM.getDLongitude());
                                        Log.d("PICKEDUP--", "dropoff");
                                    }

                                    break;
                                case MIDSTOPPED:
                                    WayPoint waypoint1 = null;
                                    String wayPointString1 = DATUM.getWay_points();
                                    try {
                                        JSONArray ary = new JSONArray(wayPointString1);
                                        if (ary.length() > 0) {
                                            waypoint1 = new WayPoint(ary.optJSONObject(0));
                                        }
                                    } catch (Exception e) {
                                        Log.d("tag", e.toString());
                                    }
                                    if (waypoint1 != null) {
                                        source = new LatLng(waypoint1.getLat(), waypoint1.getLng());
                                        destination = new LatLng(DATUM.getDLatitude(), DATUM.getDLongitude());
                                    }

                                    break;
                            }
                            if (polyLinePoints == null || polyLinePoints.size() < 2 || mPolyline == null)
                                drawRoute(source, destination);
                            else {
                                int index = checkForReRoute(source);
                                if (index <= 0) reRoutingDelay(source, destination);
                                else polyLineRerouting(source, index);
                            }

                            if (start != null) {
                                SharedHelper.putCurrentLocation(MainActivity.this, start);
                                end = start;
                            }
//                            start = source;
                            start = new LatLng(lat, lng);
                            if (end != null && canCarAnim) {
                                if ((start.latitude != end.latitude ||
                                        start.longitude != end.longitude))
                                    carAnim(srcMarker, end, start);
                            }
                        } else {
                            Log.d("mProviderLocation = ", "null");
                            mProviderLocation = null;
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("RRR ", "Failed to read value.", error.toException());
            }
        });
    }

    private void reRoutingDelay(LatLng source, LatLng destination) {
        if (canReRoute) {
            canReRoute = !canReRoute;
            drawRoute(source, destination);
            new Handler().postDelayed(() -> canReRoute = true, 8000);
        }
    }

    private void polyLineRerouting(LatLng point, int index) {
        if (index > 0) {
            polyLinePoints.subList(0, index + 1).clear();
            polyLinePoints.add(0, point);
            mPolyline.remove();

            mPolyline = googleMap.addPolyline(DirectionConverter.createPolyline
                    (this, polyLinePoints, 2, getResources().getColor(R.color.colorAccent)));

            System.out.println("RRR mPolyline = " + polyLinePoints.size());
        } else System.out.println("RRR mPolyline = Failed");
    }

    private int checkForReRoute(LatLng point) {
        if (polyLinePoints != null && polyLinePoints.size() > 0) {
            System.out.println("RRR indexOnEdgeOrPath = " +
                    new PolyUtil().indexOnEdgeOrPath(point, polyLinePoints, false, true, 100));
            //      indexOnEdgeOrPath returns -1 if the point is outside the polyline
            //      returns the index position if the point is inside the polyline
            return new PolyUtil().indexOnEdgeOrPath(point, polyLinePoints, false, true, 100);
        } else return -1;
    }

    public boolean hasOpenedDialogs() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments)
            if (fragment != null && fragment.isVisible()) return true;
        return false;
    }

    private void showDestinationAlert(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.ride_updated))
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), (dialog, id) -> updateDestination());
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carAnim(final Marker marker, final LatLng start, final LatLng end) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1900);
        final LatLngInterface latLngInterpolator = new LatLngInterface.LinearFixed();
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(valueAnimator1 -> {
            try {
                canCarAnim = false;
                float v = valueAnimator1.getAnimatedFraction();
                LatLng newPos = latLngInterpolator.interpolate(v, start, end);
                marker.setPosition(newPos);
                marker.setAnchor(0.5f, 0.5f);
                marker.setRotation(bearingBetweenLocations(start, end));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                canCarAnim = true;
            }
        });
        animator.start();
    }

    private double rotateMarker(double currentLat, double currentLng,
                                double nextLat, double nextLng) {
        double degToRad = Math.PI / 180.0;
        double phi1 = currentLat * degToRad;
        double phi2 = nextLat * degToRad;
        double lam1 = currentLng * degToRad;
        double lam2 = nextLng * degToRad;

        return Math.atan2(
                Math.sin(lam2 - lam1) * Math.cos(phi2),
                Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1)
                        * Math.cos(phi2) * Math.cos(lam2 - lam1))
                * 180 / Math.PI;
    }

    private void updateDestination() {
        STATUS = "";
        if (googleMap != null) googleMap.clear();
    }

    public void ShowLogoutPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(getString(R.string.log_out_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", SharedHelper.getKey(activity(),
                            Constants.SharedPref.USER_ID) + "");
                    presenter.logout(map);
                }).setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
            String user_id = SharedHelper.getKey(activity(), Constants.SharedPref.USER_ID);
            Utilities.printV("user_id===>", user_id);
            dialog.cancel();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void drawRoute(LatLng source, LatLng destination) {
        GoogleDirection.withServerKey(getString(R.string.google_map_key))
                .from(source)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            googleMap.clear();
            Route route = direction.getRouteList().get(0);
            if (!route.getLegList().isEmpty()) {

                Leg startLeg = route.getLegList().get(0);
                Leg endLeg = route.getLegList().get(0);

                LatLng origin = new LatLng(startLeg.getStartLocation().getLatitude(), startLeg.getStartLocation().getLongitude());
                LatLng destination = new LatLng(endLeg.getEndLocation().getLatitude(), endLeg.getEndLocation().getLongitude());

                srcMarker = googleMap.addMarker(new MarkerOptions()
                        .position(origin)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
                googleMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.des_icon)))
                        .setTag(endLeg);
            }

            polyLinePoints = route.getLegList().get(0).getDirectionPoint();
            mPolyline = googleMap.addPolyline(DirectionConverter.createPolyline
                    (this, polyLinePoints, 2, getResources().getColor(R.color.colorAccent)));
            setCameraWithCoordinationBounds(route);

        } else Toast.makeText(this, direction.getStatus(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public void navigateToShareScreen() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.checkout_app) +
                getString(R.string.app_name) + "\nhttps://play.google.com/store/apps/details?id=" +
                BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public float bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        return (float) brng;
    }

    private interface LatLngInterface {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterface {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                if (Math.abs(lngDelta) > 180) lngDelta -= Math.signum(lngDelta) * 360;
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private void initAdvertisement() {
        recyclerView_Ads.setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Ads.setLayoutManager(mLayoutManager);
        m_aryAds = new ArrayList<>();
        m_adapter = new AdsAdapter(this, m_aryAds);
        recyclerView_Ads.setAdapter(m_adapter);
        getAdvertisement();
    }

    private void getAdvertisement() {
        presenter.getAdsInfo();
    }

    @Override
    public void onAdsSuccess(List<Advertisement> array) {
        m_aryAds.clear();
        m_aryAds.addAll(array);
        m_adapter.notifyDataSetChanged();
        recyclerView_Ads.setVisibility(m_aryAds.size() > 0 ? View.VISIBLE : View.GONE);
    }

}
