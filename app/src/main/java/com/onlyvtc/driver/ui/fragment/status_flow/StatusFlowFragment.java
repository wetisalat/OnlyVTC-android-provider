package com.onlyvtc.driver.ui.fragment.status_flow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chaos.view.PinView;
import com.google.android.gms.maps.model.LatLng;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseFragment;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.common.SharedHelper;
import com.onlyvtc.driver.common.Utilities;
import com.onlyvtc.driver.common.chat.ChatActivity;
import com.onlyvtc.driver.data.network.model.Request_;
import com.onlyvtc.driver.data.network.model.TimerResponse;
import com.onlyvtc.driver.data.network.model.TripResponse;
import com.onlyvtc.driver.data.network.model.WayPoint;
import com.onlyvtc.driver.ui.activity.main.MainActivity;
import com.onlyvtc.driver.ui.activity.zoom_view.ZoomPhotoActivity;
import com.onlyvtc.driver.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.onlyvtc.driver.MvpApplication.DATUM;
import static com.onlyvtc.driver.MvpApplication.mLastKnownLocation;
import static com.onlyvtc.driver.common.Constants.User.Account.USER_TYPE_COMPANY;
import static com.onlyvtc.driver.common.Constants.checkStatus.MIDSTOPPED;
import static com.onlyvtc.driver.common.Constants.checkStatus.PICKEDUP;

public class StatusFlowFragment extends BaseFragment implements StatusFlowIView {

    protected boolean waitingTimeStatus;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_rating)
    AppCompatRatingBar userRating;
    @BindView(R.id.imgCall)
    ImageView imgCall;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnStatus)
    Button btnStatus;
    @BindView(R.id.status_arrived_img)
    ImageView statusArrivedImg;
    @BindView(R.id.status_picked_up_img)
    ImageView statusPickedUpImg;
    @BindView(R.id.status_finished_img)
    ImageView statusFinishedImg;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.imgMsg)
    ImageView imgMsg;
    @BindView(R.id.arrived_view)
    View arrivedView;
    @BindView(R.id.btWaitingTime)
    Button btWaitingTime;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.llWaitingTimeContainer)
    LinearLayout llWaitingTimeContainer;
    Unbinder unbinder;
    private StatusFlowPresenter presenter = new StatusFlowPresenter();
    private Context thisContext;
    private AlertDialog addTollDialog, otpDialog;
    private Double tollAmount;
    private Request_ data = null;
    private TripResponse tripResponse = null;
    private String STATUS = "";
    private String userProfileUrl = "";
    private String phoneNumber = "";
    private Handler customHandler = new Handler();
    private long timerInHandler = 0L;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timerInHandler++;
            secondSplitUp(timerInHandler, tvTimer);
            customHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_status_flow;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        this.thisContext = getContext();
        init();
        return view;
    }

    private void init() {
        data = DATUM;
        tripResponse = MvpApplication.tripResponse;

        if (data != null && data.getStatus() != null) {
            Utilities.printV("data===>", data.getStatus());
            changeFlow(data.getStatus());
            LatLng origin = new LatLng(data.getSLatitude(), data.getSLongitude());
            LatLng destination = new LatLng(data.getDLatitude(), data.getDLongitude());
            double lat = mLastKnownLocation.getLatitude();
            double lng = mLastKnownLocation.getLongitude();
            LatLng currentLocation = new LatLng(lat, lng);
            switch (data.getStatus()) {
                case Constants.checkStatus.ACCEPTED:
                case Constants.checkStatus.STARTED:
                    ((MainActivity) getContext()).drawRoute(currentLocation, origin);
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
                        destination = new LatLng(waypoint.getLat(), waypoint.getLng());
                    }
                    ((MainActivity) getContext()).drawRoute(currentLocation, destination);
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
                        origin = new LatLng(waypoint1.getLat(), waypoint1.getLng());
                    }
                    ((MainActivity) getContext()).drawRoute(origin, destination);

                    break;
                default:
                    break;
            }
        }

    }

    public void onCall(String phoneNum) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    111);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phoneNum)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall(phoneNumber);
                }
//                else {
//                    Toast.makeText(getContext(), "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
//                }
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.imgCall, R.id.btnCancel, R.id.btnStatus, R.id.imgMsg, R.id.btWaitingTime, R.id.user_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCall:
                onCall(phoneNumber = data.getUser().getCountry_code() + data.getUser().getMobile());
                break;
            case R.id.btnCancel:
                SharedHelper.putKey(thisContext, Constants.SharedPref.CANCEL_ID, String.valueOf(data.getId()));
                cancelRequestPopup();
                break;
            case R.id.btnStatus:
                if (STATUS.equalsIgnoreCase(Constants.checkStatus.PICKEDUP) && Constants.showOTP)
                    showOTP();
                else if (STATUS.equalsIgnoreCase(Constants.checkStatus.DROPPED))
                    showAddTollDialog();
                else statusUpdateCall(STATUS);
                break;
            case R.id.btWaitingTime:
                waitingTimeStatus = !waitingTimeStatus;
                presenter.waitingTime(waitingTimeStatus ? "1" : "0", String.valueOf(data.getId()));
                break;
            case R.id.imgMsg:
                if (DATUM != null) {
                    Intent i = new Intent(thisContext, ChatActivity.class);
                    i.putExtra(Constants.SharedPref.REQUEST_ID, String.valueOf(DATUM.getId()));
                    startActivity(i);
                }
                break;
            case R.id.user_img:
                Intent intent1 = new Intent(getContext(), ZoomPhotoActivity.class);
                intent1.putExtra(ZoomPhotoActivity.URL, userProfileUrl);
                startActivity(intent1);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void changeFlow(String status) {

        btnCancel.setVisibility(View.GONE);

        if (data.getUser().getUserType().equals(USER_TYPE_COMPANY)) {
            userName.setText(data.getUser().getCompanyName());
        } else {
            userName.setText(data.getUser().getFirstName() + " " + data.getUser().getLastName());
        }

        userRating.setRating(Float.parseFloat(data.getUser().getRating()));

        Glide.with(thisContext).
                load(BuildConfig.BASE_IMAGE_URL +
                        data.getUser().getPicture()).
                apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                        dontAnimate().error(R.drawable.ic_user_placeholder)).into(userImg);
        userProfileUrl = BuildConfig.BASE_IMAGE_URL + data.getUser().getPicture();
        if (Constants.checkStatus.PICKEDUP.equalsIgnoreCase(status)) {
            imgMsg.setVisibility(View.GONE);
            llWaitingTimeContainer.setVisibility(View.VISIBLE);
        } else {
            imgMsg.setVisibility(View.VISIBLE);
            llWaitingTimeContainer.setVisibility(View.GONE);
        }

        switch (status) {
            case Constants.checkStatus.ACCEPTED:
                btnStatus.setText(getString(R.string.arrived));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.STARTED;
                break;
            case Constants.checkStatus.STARTED:
                btnStatus.setText(getString(R.string.arrived));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.ARRIVED;
                break;
            case Constants.checkStatus.ARRIVED:
                btnStatus.setText(getString(R.string.pick_up_invoice));
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = Constants.checkStatus.PICKEDUP;
                statusArrivedImg.setImageResource(R.drawable.ic_arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.ic_pickup);
                statusFinishedImg.setImageResource(R.drawable.ic_finished);
                break;
            case Constants.checkStatus.PICKEDUP:
                arrivedView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryText));
                btnStatus.setBackgroundResource(R.drawable.button_round_primary);

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
                    btnStatus.setText(getString(R.string.tap_when_stopped));
                    STATUS = Constants.checkStatus.MIDSTOPPED;
                } else {
                    btnStatus.setText(getString(R.string.tap_when_dropped));
                    STATUS = Constants.checkStatus.DROPPED;
                }

                statusArrivedImg.setImageResource(R.drawable.ic_arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.ic_pickup_select);
                statusFinishedImg.setImageResource(R.drawable.ic_finished);
                break;

            case Constants.checkStatus.MIDSTOPPED:
                arrivedView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryText));
                btnStatus.setBackgroundResource(R.drawable.button_round_primary);
                btnStatus.setText(getString(R.string.tap_when_dropped));
                STATUS = Constants.checkStatus.DROPPED;
                statusArrivedImg.setImageResource(R.drawable.ic_arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.ic_pickup_select);
                statusFinishedImg.setImageResource(R.drawable.ic_finished);

                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .remove(StatusFlowFragment.this).commit();
        getContext().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onWaitingTimeSuccess(TimerResponse object) {
        timerInHandler = object.getWaitingTime();
        secondSplitUp(timerInHandler, tvTimer);
        if (object.getWaitingStatus() == 1) customHandler.postDelayed(updateTimerThread, 1000);
        else customHandler.removeCallbacks(updateTimerThread);
    }

    public void secondSplitUp(long biggy, TextView tvTimer) {
        int hours = (int) biggy / 3600;
        int sec = (int) biggy - hours * 3600;
        int mins = sec / 60;
        sec = sec - mins * 60;
        tvTimer.setText(String.format("%02d:", hours)
                + String.format("%02d:", mins)
                + String.format("%02d", sec));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        try {
            if (e != null)
                onErrorBase(e);
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
    }

    void statusUpdateCall(String status) {
        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", status);
            map.put("_method", "PATCH");
            if (tollAmount != null)
                map.put("toll_price", tollAmount);
            if (mLastKnownLocation != null) {
                map.put("latitude", mLastKnownLocation.getLatitude());
                map.put("longitude", mLastKnownLocation.getLongitude());
            }
            showLoading();
            presenter.statusUpdate(map, datum.getId());
        }
    }

    void cancelRequestPopup() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisContext);
            alertDialogBuilder
                    .setMessage(thisContext.getResources().getString(R.string.cancel_request_title))
                    .setCancelable(false)
                    .setPositiveButton(thisContext.getResources().getString(R.string.yes), (dialog, id) -> {
                        try {
                            CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                            cancelDialogFragment.show(getActivity().getSupportFragmentManager(), cancelDialogFragment.getTag());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).setNegativeButton(thisContext.getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.otp_dialog, null);

        Button submitBtn = view.findViewById(R.id.submit_btn);
        final PinView pinView = view.findViewById(R.id.pinView);

        builder.setView(view);
        otpDialog = builder.create();
        otpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitBtn.setOnClickListener(view1 -> {

            if (data.getOtp().equalsIgnoreCase(pinView.getText().toString())) {
                try {
                    if (thisContext != null)
                        Toasty.success(thisContext, thisContext.getResources().getString(R.string.otp_verified), Toast.LENGTH_SHORT).show();
                    statusUpdateCall(STATUS);
                    otpDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else try {
                if (thisContext != null && isAdded())
                    Toasty.error(thisContext, thisContext.getResources().getString(R.string.otp_wrong), Toast.LENGTH_SHORT).show();
                else
                    Toasty.error(thisContext, thisContext.getResources().getString(R.string.otp_wrong), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        otpDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkWaitingTime(String.valueOf(data.getId()));
    }

    private void showAddTollDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_toll, null);

        Button submitBtn = view.findViewById(R.id.submit_btn);
        Button dismiss = view.findViewById(R.id.dismiss);
        final EditText toll_amount = view.findViewById(R.id.toll_amount);
        final TextView currency_type = view.findViewById(R.id.currency_type);

        currency_type.setText(Constants.Currency);

        builder.setView(view);
        addTollDialog = builder.create();
        Objects.requireNonNull(addTollDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitBtn.setOnClickListener(view1 -> {
            if (!toll_amount.getText().toString().trim().equalsIgnoreCase("")
                    && Double.parseDouble(toll_amount.getText().toString().trim()) > 0)
                tollAmount = Double.valueOf(toll_amount.getText().toString());
            else tollAmount = Double.valueOf("0");
            customHandler.removeCallbacks(updateTimerThread);
            statusUpdateCall(STATUS);
            addTollDialog.dismiss();
        });

        dismiss.setOnClickListener(v -> {
            tollAmount = null;
            addTollDialog.dismiss();
            statusUpdateCall(STATUS);
        });

        addTollDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customHandler.removeCallbacks(updateTimerThread);
    }
}
