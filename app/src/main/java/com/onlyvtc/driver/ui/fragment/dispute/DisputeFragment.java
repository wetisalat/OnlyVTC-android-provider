package com.onlyvtc.driver.ui.fragment.dispute;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseBottomSheetDialogFragment;
import com.onlyvtc.driver.common.Constants;
import com.onlyvtc.driver.common.LocaleHelper;
import com.onlyvtc.driver.common.SharedHelper;
import com.onlyvtc.driver.data.network.model.DisputeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onlyvtc.driver.MvpApplication.DATUM_history;

public class DisputeFragment extends BaseBottomSheetDialogFragment implements DisputeIView {

    @BindView(R.id.cancel_reason)
    EditText cancelReason;
    @BindView(R.id.rcvReason)
    RecyclerView rcvReason;
    private DisputePresenter<DisputeFragment> presenter = new DisputePresenter<>();
    private List<DisputeResponse> disputeResponseList = new ArrayList<>();
    private DisputeFragment.DisputeAdapter adapter;
    private int lastSelectedLocation = -1;
    private DisputeCallBack mCallBack;

    public DisputeFragment() {
    }

    public void setCallBack(DisputeCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dispute_dialog;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        presenter.attachView(this);

        adapter = new DisputeFragment.DisputeAdapter(disputeResponseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvReason.setLayoutManager(mLayoutManager);
        rcvReason.setItemAnimator(new DefaultItemAnimator());
        rcvReason.setAdapter(adapter);

        presenter.getDispute();
    }

    @OnClick({R.id.dismiss, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dismiss:
                dismiss();
                break;
            case R.id.submit:
                if (lastSelectedLocation == -1) {
                    Toast.makeText(getContext(), getString(R.string.invalid_selection), Toast.LENGTH_SHORT).show();
                    return;
                }
                createDispute();
                break;
        }
    }

    private void createDispute() {
        /*if (cancelReason.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.invalid_dispute_comment), Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (DATUM_history != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("request_id", DATUM_history.getId());
            map.put("user_id", DATUM_history.getUserId());
            map.put("dispute_type", "provider");
            map.put("provider_id", SharedHelper.getKey(Objects.requireNonNull(getContext()), Constants.SharedPref.USER_ID));
            map.put("comments", cancelReason.getText().toString());
            map.put("dispute_name", disputeResponseList.get(lastSelectedLocation).getDispute_name());

            showLoading();
            presenter.dispute(map);
        }
    }

    @Override
    public void onSuccessDispute(List<DisputeResponse> responseList) {
        disputeResponseList.addAll(responseList);
        setDefaultSelection();
    }

    @Override
    public void onSuccess(Object object) {
        try {
            hideLoading();
            if (object instanceof LinkedTreeMap) {
                LinkedTreeMap responseMap = (LinkedTreeMap) object;
                if (responseMap.get("message") != null) {
                    if (mCallBack != null)
                        mCallBack.onDisputeCreated();
                    Toast.makeText(getActivity().getApplicationContext(),
                            responseMap.get("message").toString(), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        getResources().getString(R.string.lost_item_error), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        dismiss();
    }

    private void setDefaultSelection() {
        lastSelectedLocation = -1;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {
        onErrorBase(e);
    }

    private class DisputeAdapter extends RecyclerView.Adapter<DisputeAdapter.MyViewHolder> {

        private List<DisputeResponse> list;

        private DisputeAdapter(List<DisputeResponse> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public DisputeFragment.DisputeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DisputeFragment.DisputeAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cancel_reasons_inflate, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DisputeFragment.DisputeAdapter.MyViewHolder holder, int position) {
            String strDisputeName;
            DisputeResponse data = list.get(position);

            strDisputeName = data.getDispute_name();

            String dd = LocaleHelper.getLanguage(getContext());
            switch (dd) {
                case Constants.Language.FRENCH:
                    if (strDisputeName.equalsIgnoreCase("user not familiar with route and changed route")) {
                        strDisputeName = "utilisateur non familier avec l'itinéraire et l'itinéraire modifié";
                    } else if (strDisputeName.equalsIgnoreCase("user arrogant and rude")) {
                        strDisputeName = "utilisateur arrogant et grossier";
                    } else if (strDisputeName.equalsIgnoreCase("user not paid amount")) {
                        strDisputeName = "montant non payé par l'utilisateur";
                    }
                    break;
                case Constants.Language.ARABIC:
                    if (strDisputeName.equalsIgnoreCase("user not familiar with route and changed route")) {
                        strDisputeName = "المستخدم ليس على دراية الطريق وتغيير المسار";
                    } else if (strDisputeName.equalsIgnoreCase("user arrogant and rude")) {
                        strDisputeName = "المستخدم متعجرف وقحا";
                    } else if (strDisputeName.equalsIgnoreCase("user not paid amount")) {
                        strDisputeName = "المستخدم لم يدفع المبلغ";
                    }
                    break;
            }

            holder.tvReason.setText(strDisputeName);
            holder.cbItem.setChecked(lastSelectedLocation == position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout llItemView;
            TextView tvReason;
            CheckBox cbItem;

            MyViewHolder(View view) {
                super(view);
                llItemView = view.findViewById(R.id.llItemView);
                tvReason = view.findViewById(R.id.tvReason);
                cbItem = view.findViewById(R.id.cbItem);
                llItemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                lastSelectedLocation = getAdapterPosition();
                notifyDataSetChanged();
            }
        }
    }
}
