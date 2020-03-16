package com.onlyvtc.driver.ui.activity.currentride;

import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseActivity;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.data.network.model.Provider;
import com.onlyvtc.driver.data.network.model.WayPoint;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.onlyvtc.driver.MvpApplication.DATUM;
import static com.onlyvtc.driver.MvpApplication.tripResponse;

public class CurrentRideActivity extends BaseActivity {

    @BindView(R.id.civDriverAvatar)
    CircleImageView civDriverAvatar;
    @BindView(R.id.tvDriverName)
    TextView tvDriverName;
    @BindView(R.id.rating_driver)
    AppCompatRatingBar rating_driver;
    @BindView(R.id.ivCar)
    ImageView ivCar;
    @BindView(R.id.tvCarModel)
    TextView tvCarModel;
    @BindView(R.id.civPassenger)
    CircleImageView civPassenger;
    @BindView(R.id.tvPassengerName)
    TextView tvPassengerName;
    @BindView(R.id.rating_passenger)
    AppCompatRatingBar rating_passenger;
    @BindView(R.id.tvBookingIDValue)
    TextView tvBookingIDValue;
    @BindView(R.id.tvBookingDateTimeValue)
    TextView tvBookingDateTimeValue;
    @BindView(R.id.tvPickupDateTime)
    TextView tvPickupDateTime;
    @BindView(R.id.tvPickupLocation)
    TextView tvPickupLocation;
    @BindView(R.id.tvStopLocation)
    TextView tvStopLocation;
    @BindView(R.id.tvDropoffValue)
    TextView tvDropoffValue;
    @BindView(R.id.ivPaymentMode)
    ImageView ivPaymentMode;
    @BindView(R.id.tvPaymentMode)
    TextView tvPaymentMode;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.container_empty)
    LinearLayout container_empty;
    @BindView(R.id.container_stop_location)
    LinearLayout container_stop_location;


    @Override
    public int getLayoutId() {
        return R.layout.activity_current_ride;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.current_ride));
        boolean isNotEmptyRide = DATUM != null && (DATUM.getStatus().equalsIgnoreCase(Constants.checkStatus.STARTED) ||
                DATUM.getStatus().equalsIgnoreCase(Constants.checkStatus.ARRIVED) ||
                DATUM.getStatus().equalsIgnoreCase(Constants.checkStatus.PICKEDUP) ||
                DATUM.getStatus().equalsIgnoreCase(Constants.checkStatus.MIDSTOPPED) ||
                DATUM.getStatus().equalsIgnoreCase(Constants.checkStatus.DROPPED));
        if (isNotEmptyRide) {
            container_empty.setVisibility(View.GONE);
            showDetail();
        }

    }

    private void showDetail() {
        // Driver Information
        Provider provider = tripResponse.getProviderDetails();

        Glide.with(activity())
                .load(BuildConfig.BASE_IMAGE_URL + provider.getAvatar())
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate()
                        .error(R.drawable.ic_user_placeholder))
                .into(civDriverAvatar);
        tvDriverName.setText(provider.getFirstName() + " " + provider.getLastName());
        rating_driver.setRating(Float.parseFloat(provider.getRating()));

        // Car Service
        Glide.with(activity())
                .load(provider.getService().getServiceType().getImage())
                .into(ivCar);
        tvCarModel.setText(provider.getService().getServiceType().getName() + "\n" + provider.getService().getServiceModel() + "\n" + provider.getService().getServiceNumber());

        // Passenger Information
        Glide.with(activity())
                .load(BuildConfig.BASE_IMAGE_URL + DATUM.getUser().getPicture())
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate()
                        .error(R.drawable.ic_user_placeholder))
                .into(civPassenger);
        tvPassengerName.setText(DATUM.getUser().getFirstName() + "" + DATUM.getUser().getLastName());
        rating_passenger.setRating(Float.parseFloat(DATUM.getUser().getRating()));

        // Booking Information
        tvBookingIDValue.setText(DATUM.getBookingId());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        SimpleDateFormat timeFormat;
        Date newDate;
        try {
            newDate = format.parse(DATUM.getCreatedAt());
            format = new SimpleDateFormat("dd-MM-yyyy");
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = format.format(newDate);
            String time = timeFormat.format(newDate);
            tvBookingDateTimeValue.setText(date + " " + time);

            newDate = format.parse(DATUM.getStartedAt());
            date = format.format(newDate);
            time = timeFormat.format(newDate);
            tvPickupDateTime.setText(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvPickupLocation.setText(DATUM.getSAddress());

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
            tvStopLocation.setText(waypoint.getAddress());
        } else {
            container_stop_location.setVisibility(View.GONE);
        }

        tvDropoffValue.setText(DATUM.getDAddress());


        // Ride cost Information
        initPayment(DATUM.getPaymentMode());
        tvPrice.setText(MvpApplication.getInstance().getNewNumberFormat(DATUM.getTotal_price()));

    }

    private void initPayment(String paymentMode) {

        switch (paymentMode) {
            case Constants.PaymentMode.CASH:
                ivPaymentMode.setImageResource(R.drawable.ic_money);
                tvPaymentMode.setText(getString(R.string.cash));
                break;
            case Constants.PaymentMode.CARD:
                ivPaymentMode.setImageResource(R.drawable.ic_card);
                tvPaymentMode.setText(getString(R.string.card));
                break;
            case Constants.PaymentMode.PAYPAL:
                tvPaymentMode.setText(getString(R.string.paypal));
                break;
            case Constants.PaymentMode.WALLET:
                tvPaymentMode.setText(getString(R.string.wallet));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
