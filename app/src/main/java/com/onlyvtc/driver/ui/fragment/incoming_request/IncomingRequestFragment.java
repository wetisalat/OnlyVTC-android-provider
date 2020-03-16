package com.onlyvtc.driver.ui.fragment.incoming_request;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseFragment;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.common.Utilities;
import com.onlyvtc.driver.data.network.model.Request_;
import com.onlyvtc.driver.data.network.model.WayPoint;
import com.onlyvtc.driver.ui.activity.zoom_view.ZoomPhotoActivity;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.onlyvtc.driver.MvpApplication.DATUM;
import static com.onlyvtc.driver.MvpApplication.time_to_left;
import static com.onlyvtc.driver.common.Constants.User.Account.USER_TYPE_COMPANY;

public class IncomingRequestFragment extends BaseFragment implements IncomingRequestIView {

    @BindView(R.id.btnReject)
    Button btnReject;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    Unbinder unbinder;
    @BindView(R.id.lblCount)
    TextView lblCount;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.lblUserName)
    TextView lblUserName;
    @BindView(R.id.ratingUser)
    AppCompatRatingBar ratingUser;
    @BindView(R.id.lblLocationName)
    TextView lblLocationName;
    @BindView(R.id.lblDrop)
    TextView lblDrop;
    @BindView(R.id.lblScheduleDate)
    TextView lblScheduleDate;
    @BindView(R.id.container_stop)
    LinearLayout container_stop;
    @BindView(R.id.lblStop)
    TextView lblStop;
    @BindView(R.id.ivPaymentType)
    ImageView ivPaymentType;
    @BindView(R.id.lblPaymentType)
    TextView lblPaymentType;
    @BindView(R.id.lblAmount)
    TextView lblAmount;
    @BindView(R.id.lbNote)
    TextView lbNote;
    @BindView(R.id.lbNoteValue)
    TextView lbNoteValue;

    private String userProfileUrl = "";
    private IncomingRequestPresenter presenter = new IncomingRequestPresenter();
    private Context context;
    public static CountDownTimer countDownTimer;
    public static MediaPlayer mPlayer;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_incoming_request;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
        presenter.attachView(this);
        mPlayer = MediaPlayer.create(context, R.raw.alert_tone);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    void init() {
        Request_ data = DATUM;
        if (data != null) {

            if (data.getUser().getUserType().equals(USER_TYPE_COMPANY)) {
                lblUserName.setText(data.getUser().getCompanyName());
            } else {
                lblUserName.setText(String.format("%s %s", data.getUser().getFirstName(),
                        data.getUser().getLastName()));
            }

            ratingUser.setRating(Float.parseFloat(data.getUser().getRating()));
            if (data.getSAddress() != null && !data.getSAddress().isEmpty()
                    || data.getDAddress() != null && !data.getDAddress().isEmpty()) {
                lblLocationName.setText(data.getSAddress());
                lblDrop.setText(data.getDAddress());
            }
            if (data.getUser().getPicture() != null) {
                Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL + data.getUser()
                        .getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate().error(R.drawable.ic_user_placeholder)).into(imgUser);
                userProfileUrl = BuildConfig.BASE_IMAGE_URL + data.getUser().getPicture();
            }
        }

        String isScheduled = data.getIsScheduled();
        String scheduledAt = data.getScheduleAt();
        if (isScheduled != null && isScheduled.equalsIgnoreCase("NO")) {
            lblScheduleDate.setVisibility(View.INVISIBLE);
        } else {
            if (scheduledAt != null && isScheduled.equalsIgnoreCase("YES")) {
                StringTokenizer tk = new StringTokenizer(scheduledAt);
                String date = tk.nextToken();
                String time = tk.nextToken();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdfs = new SimpleDateFormat("HH:mm");
                Date dt;
                try {
                    dt = sdf.parse(time);
                    lblScheduleDate.setVisibility(View.VISIBLE);
                    lblScheduleDate.setText(getString(R.string.schedule_for) + " " +
                            Utilities.convertDate(scheduledAt) + " " + sdfs.format(dt));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        WayPoint waypoint = null;
        String wayPointString = data.getWay_points();
        try {
            JSONArray ary = new JSONArray(wayPointString);
            if (ary.length() > 0) {
                waypoint = new WayPoint(ary.optJSONObject(0));
            }
        } catch (Exception e) {
            Log.d("tag", e.toString());
        }
        if (waypoint != null) {
            lblStop.setText(waypoint.getAddress());
        } else {
            container_stop.setVisibility(View.GONE);
        }

        String strNote = data.getNote() != null ? data.getNote() : "";
        if (strNote.isEmpty()) {
            lbNoteValue.setVisibility(View.GONE);
            lbNote.setVisibility(View.GONE);
        } else {
            lbNoteValue.setText(strNote);
        }

        initPayment(data.getPaymentMode());
        lblAmount.setText(MvpApplication.getInstance().getNewNumberFormat(data.getTotal_price()));

        countDownTimer = new CountDownTimer(time_to_left * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                lblCount.setText(String.valueOf(millisUntilFinished / 1000));
                setTvZoomInOutAnimation(lblCount);
            }

            public void onFinish() {
                try {
                    context.sendBroadcast(new Intent("INTENT_FILTER"));
                    mPlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        countDownTimer.start();
    }

    void initPayment(String paymentMode) {

        switch (paymentMode) {
            case Constants.PaymentMode.CASH:
                ivPaymentType.setImageResource(R.drawable.ic_money);
                lblPaymentType.setText(getString(R.string.cash));
                break;
            case Constants.PaymentMode.CARD:
                ivPaymentType.setImageResource(R.drawable.ic_card);
                lblPaymentType.setText(getString(R.string.card));
                break;
            case Constants.PaymentMode.PAYPAL:
                lblPaymentType.setText(getString(R.string.paypal));
                break;
            case Constants.PaymentMode.WALLET:
                lblPaymentType.setText(getString(R.string.wallet));
                break;
            default:
                break;
        }
    }

    private void setTvZoomInOutAnimation(final TextView textView) {
        final float startSize = 20;
        final float endSize = 13;
        final int animationDuration = 900; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(valueAnimator -> {
            float animatedValue = (Float) valueAnimator.getAnimatedValue();
            textView.setTextSize(animatedValue);
        });
        //animator.setRepeatCount(ValueAnimator.INFINITE);  // Use this line for infinite animations
        animator.setRepeatCount(2);
        animator.start();
    }


    @OnClick({R.id.btnReject, R.id.btnAccept, R.id.imgUser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnReject:
                if (DATUM != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.cancel(datum.getId());
                    time_to_left = 60;
                }
                break;
            case R.id.btnAccept:
                if (DATUM != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.accept(datum.getId());
                    time_to_left = 60;
                }
                break;
            case R.id.imgUser:
                Intent intent1 = new Intent(getContext(), ZoomPhotoActivity.class);
                intent1.putExtra(ZoomPhotoActivity.URL, userProfileUrl);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onSuccessAccept(Object responseBody) {
        countDownTimer.cancel();
        hideLoading();
        Toast.makeText(getContext(), getString(R.string.ride_accepted), Toast.LENGTH_SHORT).show();
        getContext().sendBroadcast(new Intent("INTENT_FILTER"));
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessCancel(Object object) {
        countDownTimer.cancel();
        hideLoading();
        getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commitAllowingStateLoss();
        Toasty.success(context, getString(R.string.ride_cancelled), Toast.LENGTH_SHORT, true).show();
        context.sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {
        try {
            hideLoading();
            if (mPlayer.isPlaying()) mPlayer.stop();
            if (e != null)
                onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mPlayer.isPlaying())
            mPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }
}
