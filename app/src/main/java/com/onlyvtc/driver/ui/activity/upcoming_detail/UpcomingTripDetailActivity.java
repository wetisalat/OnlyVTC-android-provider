package com.onlyvtc.driver.ui.activity.upcoming_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseActivity;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.common.SharedHelper;
import com.onlyvtc.driver.data.network.model.HistoryDetail;
import com.onlyvtc.driver.data.network.model.User_Past;
import com.onlyvtc.driver.data.network.model.WayPoint;
import com.onlyvtc.driver.ui.activity.zoom_view.ZoomPhotoActivity;
import com.onlyvtc.driver.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.onlyvtc.driver.MvpApplication.DATUM_history;
import static com.onlyvtc.driver.MvpApplication.DATUM_history_detail;

public class UpcomingTripDetailActivity extends BaseActivity implements UpcomingTripDetailIView, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.call)
    Button call;
    @BindView(R.id.static_map)
    ImageView staticMap;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.first_name)
    TextView firstName;
    @BindView(R.id.rating)
    AppCompatRatingBar rating;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.schedule_at)
    TextView scheduleAt;
    @BindView(R.id.lblSource)
    TextView lblSource;
    @BindView(R.id.lblDestination)
    TextView lblDestination;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.payable)
    TextView payable;
    @BindView(R.id.upcoming_payment)
    ImageView upcomingPayment;
    @BindView(R.id.checkbox0)
    CheckBox checkbox0;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.checkbox3)
    CheckBox checkbox3;
    @BindView(R.id.checkbox4)
    CheckBox checkbox4;
    @BindView(R.id.checkbox5)
    CheckBox checkbox5;
    @BindView(R.id.checkbox6)
    CheckBox checkbox6;
    @BindView(R.id.sep0)
    View sep0;
    @BindView(R.id.sep1)
    View sep1;
    @BindView(R.id.sep2)
    View sep2;
    @BindView(R.id.sep3)
    View sep3;
    @BindView(R.id.sep4)
    View sep4;
    @BindView(R.id.sep5)
    View sep5;
    @BindView(R.id.sep6)
    View sep6;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.lblStop)
    TextView lblStop;

    private UpcomingTripDetailPresenter presenter = new UpcomingTripDetailPresenter();
    private String strUserImgUrl = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_upcoming_trip_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.upcoming_trip_details));
        if (DATUM_history != null) {
            presenter.getUpcomingDetail(String.valueOf(DATUM_history.getId()));

        }
    }


    void initPayment(String mode) {

        switch (mode) {
            case Constants.PaymentMode.CASH:
                paymentMode.setText(getString(R.string.cash));
                upcomingPayment.setImageResource(R.drawable.ic_money);
                // paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                break;
            case Constants.PaymentMode.CARD:
                paymentMode.setText(getString(R.string.card));
                upcomingPayment.setImageResource(R.drawable.ic_card);
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                break;
            case Constants.PaymentMode.PAYPAL:
                paymentMode.setText(getString(R.string.paypal));
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_paypal, 0, 0, 0);
                break;
            case Constants.PaymentMode.WALLET:
                paymentMode.setText(getString(R.string.wallet));
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wallet, 0, 0, 0);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.cancel, R.id.call, R.id.avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                SharedHelper.putKey(getBaseContext(), Constants.SharedPref.CANCEL_ID, String.valueOf(DATUM_history.getId()));
                cancelRequestPopup();
                break;
            case R.id.call:
                callTask();
                break;
            case R.id.avatar:
                Intent intent = new Intent(this, ZoomPhotoActivity.class);
                intent.putExtra(ZoomPhotoActivity.URL, strUserImgUrl);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(HistoryDetail historyDetail) {

        DATUM_history_detail = historyDetail;

        bookingId.setText(historyDetail.getBookingId());
//        scheduleAt.setText(historyDetail.getScheduleAt());
        String strCurrentDate = historyDetail.getScheduleAt();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        SimpleDateFormat timeFormat;
        Date newDate;
        try {
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("dd-MM-yyyy");
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = format.format(newDate);
            String time = timeFormat.format(newDate);
            scheduleAt.setText(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lblSource.setText(historyDetail.getSAddress());
        lblDestination.setText(historyDetail.getDAddress());
        Glide.with(activity()).load(historyDetail.getStaticMap()).
                apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).dontAnimate().
                        error(R.drawable.ic_launcher_background)).into(staticMap);
        initPayment(historyDetail.getPaymentMode());
        User_Past user = historyDetail.getUser();
        if (user != null) {
            firstName.setText(user.getFirstName());
            Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL +
                    user.getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                    dontAnimate().error(R.drawable.ic_user_placeholder)).into(avatar);
            strUserImgUrl = BuildConfig.BASE_IMAGE_URL +
                    user.getPicture();
        }

        User_Past ratingValue = historyDetail.getUser();
        if (ratingValue.getRating() != null) {
            rating.setRating(Float.parseFloat(String.valueOf(ratingValue.getRating())));
        } else {
            rating.setRating(0);
        }

        if (DATUM_history_detail.getRepeated_date() == null) {
            checkbox0.setVisibility(View.GONE);
            sep0.setVisibility(View.GONE);
            checkbox1.setVisibility(View.GONE);
            sep1.setVisibility(View.GONE);
            checkbox2.setVisibility(View.GONE);
            sep2.setVisibility(View.GONE);
            checkbox3.setVisibility(View.GONE);
            sep3.setVisibility(View.GONE);
            checkbox4.setVisibility(View.GONE);
            sep4.setVisibility(View.GONE);
            checkbox5.setVisibility(View.GONE);
            sep5.setVisibility(View.GONE);
            checkbox6.setVisibility(View.GONE);
            sep6.setVisibility(View.GONE);
        } else {
            checkbox0.setChecked(DATUM_history_detail.getRepeated().contains("0"));
            checkbox1.setChecked(DATUM_history_detail.getRepeated().contains("1"));
            checkbox2.setChecked(DATUM_history_detail.getRepeated().contains("2"));
            checkbox3.setChecked(DATUM_history_detail.getRepeated().contains("3"));
            checkbox4.setChecked(DATUM_history_detail.getRepeated().contains("4"));
            checkbox5.setChecked(DATUM_history_detail.getRepeated().contains("5"));
            checkbox6.setChecked(DATUM_history_detail.getRepeated().contains("6"));

        }

        if (historyDetail.getStatus().equals("SCHEDULED") && historyDetail.getManual_assigned_at() != null) {
            call.setVisibility(View.GONE);
        } else {
            call.setVisibility(View.VISIBLE);
        }

        note.setText(historyDetail.getNote() == null ? "" : historyDetail.getNote());
        payable.setText(MvpApplication.getInstance().getNewNumberFormat(
                Double.parseDouble(String.valueOf(Double.valueOf(historyDetail.getEstimated().getEstimatedFare())))));

        WayPoint waypoint1 = null;
        String wayPointString1 = historyDetail.getWay_points();
        try {
            JSONArray ary = new JSONArray(wayPointString1);
            if (ary.length() > 0) {
                waypoint1 = new WayPoint(ary.optJSONObject(0));
            }
        } catch (Exception e) {
            Log.d("tag", e.toString());
        }
        if (waypoint1 != null) {
            lblStop.setText(waypoint1.getAddress() == null ? "" : waypoint1.getAddress());
        } else {
            lblStop.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @SuppressLint("MissingPermission")
    void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }


    void cancelRequestPopup() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity());
        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.cancel_request_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                    cancelDialogFragment.show(getSupportFragmentManager(), cancelDialogFragment.getTag());
                }).setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    private boolean hasCallPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE);
    }

    @AfterPermissionGranted(Constants.RC_CALL_PHONE)
    public void callTask() {
        if (hasCallPermission()) {
            // Have permission, do the thing!
//            Toast.makeText(this, "TODO: CALL things", Toast.LENGTH_LONG).show();

            makeCall(DATUM_history_detail.getUser().getCountry_code() + DATUM_history_detail.getUser().getMobile());

        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this, getString(R.string.call_permission),
                    Constants.RC_CALL_PHONE,
                    Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Un Used
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Un Used
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
