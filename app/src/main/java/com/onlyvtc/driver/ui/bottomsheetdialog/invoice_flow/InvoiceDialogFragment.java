package com.onlyvtc.driver.ui.bottomsheetdialog.invoice_flow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseFragment;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.data.network.model.Request_;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.onlyvtc.driver.MvpApplication.DATUM;

public class InvoiceDialogFragment extends BaseFragment implements InvoiceDialogIView {

    @BindView(R.id.promotion_amount)
    TextView promotionAmount;
    @BindView(R.id.wallet_amount)
    TextView walletAmount;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.payable_amount)
    TextView payableAmount;
    @BindView(R.id.payment_mode_img)
    ImageView paymentModeImg;
    @BindView(R.id.payment_mode_layout)
    LinearLayout paymentModeLayout;
    @BindView(R.id.llAmountToBePaid)
    LinearLayout llAmountToBePaid;
    Unbinder unbinder;
    @BindView(R.id.btnConfirmPayment)
    Button btnConfirmPayment;

    InvoiceDialogPresenter presenter;
    @BindView(R.id.lblPaymentType)
    TextView lblPaymentType;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice_dialog;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter = new InvoiceDialogPresenter();
        presenter.attachView(this);
        // setCancelable(false);
        if (DATUM != null) {
            Request_ datum = DATUM;

            bookingId.setText(datum.getBookingId());

            if (datum.getUseWallet() == 1) {
                totalAmount.setText(MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getTotal() + "")));
                llAmountToBePaid.setVisibility(View.VISIBLE);
                payableAmount.setText(MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getPayable() + "")));
                lblPaymentType.setText(getString(R.string.wallet));
            } else {
                if (datum.getPayment() != null) {
                    Log.d("asdfasdf=>", datum.getPayment().toString());
                    if (datum.getPayment().getTotal() > 0)
                        totalAmount.setText(MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getTotal() + "")));
                    if (datum.getPayment().getPayable() > 0) {
                        llAmountToBePaid.setVisibility(View.VISIBLE);
                        payableAmount.setText(MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(datum.getPayment().getPayable() + "")));
                    } else llAmountToBePaid.setVisibility(View.GONE);

                    if (!convResFromPayStr(datum.getPaymentMode()).equalsIgnoreCase(""))
                        lblPaymentType.setText(convResFromPayStr(datum.getPaymentMode()));
                }
            }
        }
        return view;
    }

    public String convResFromPayStr(String str) {

        switch (str) {
            case Constants.PaymentMode.CARD:
                return getString(R.string.card);
            case Constants.PaymentMode.CASH:
                return getString(R.string.cash);
            case Constants.PaymentMode.PAYPAL:
                return getString(R.string.paypal);
        }
        return "";
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        try {
            if (e != null)
                onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @OnClick(R.id.btnConfirmPayment)
    public void onViewClicked() {

        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", Constants.checkStatus.COMPLETED);
            map.put("_method", Constants.HTTP_METHOD.PATCH);
            showLoading();
            presenter.statusUpdate(map, datum.getId());
        }
    }

   /* @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter = new InvoiceDialogPresenter();
        presenter.attachView(this);
        setCancelable(false);
        numberFormat = MvpApplication.getInstance().getNewNumberFormat();

        if (DATUM != null) {
            Request_ datum = DATUM;
            bookingId.setText(datum.getBookingId());
            if (datum.getPayment() != null)
                if (datum.getPayment().getTotal() != 0 ||
                        datum.getPayment().getPayable() != 0) {
                    totalAmount.setText(Constants.Currency + " " + numberFormat.format(Double.parseDouble(datum.getPayment().getTotal() + "")));
                    payableAmount.setText(Constants.Currency + " " + numberFormat.format(Double.parseDouble(datum.getPayment().getPayable() + "")));
                }
        }
    }

    *//*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "InvoiceDialogFragment", InvoiceDialogFragment.this);
    }*//*

    @OnClick(R.ID.btnConfirmPayment)
    public void onViewClicked() {

        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", Constants.checkStatus.COMPLETED);
            map.put("_method", Constants.checkStatus.PATCH);
            showLoading();
            presenter.statusUpdate(map, datum.getId());
        }
    }

    @Override
    public void onSuccess(Object object) {
        dismissAllowingStateLoss();
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
    }*/
}
