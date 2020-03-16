package com.onlyvtc.driver.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.onlyvtc.driver.MvpApplication;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.data.network.model.HistoryList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.MyViewHolder> {

    private List<HistoryList> list;
    private Context context;

    private ClickListener clickListener;

    public PastTripAdapter(List<HistoryList> list, Context con) {
        this.list = list;
        this.context = con;
    }

    public void setList(List<HistoryList> list) {
        this.list = list;
    }

    public void setClickListener(PastTripAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_past_trip, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryList historyList = list.get(position);

//        holder.finishedAt.setText(historyList.getFinishedAt());
        String strCurrentDate = historyList.getFinishedAt();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        SimpleDateFormat timeFormat;
        Date newDate;
        try {
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("dd-MM-yyyy");
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            String date = format.format(newDate);
            String time = timeFormat.format(newDate);
            holder.finishedAt.setText(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.bookingId.setText(historyList.getBookingId());
        if (historyList.getPayment() != null) {

            holder.payable.setText(MvpApplication.getInstance().getNewNumberFormat(historyList.getPayment().getTotal()));
//            holder.payable.setText(Constants.Currency + " " +
//                            MvpApplication.getInstance().getNewNumberFormat(historyList.getPayment().getTotal())
//                    /*numberFormat.format(historyList.getPayment().getTotal())*/);

        } else holder.payable.setText(MvpApplication.getInstance().getNewNumberFormat(0));
//        holder.payable.setText(Constants.Currency + " " + "0.00");
        if (historyList.getServicetype() != null)
            holder.lblServiceName.setText(historyList.getServicetype().getName());
        Glide.with(context)
                .load(historyList.getStaticMap())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background)
                        .dontAnimate().error(R.drawable.ic_launcher_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.staticMap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickListener {
        void redirectClick(HistoryList historyList, ImageView staticMap);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView staticMap;
        private CardView itemView;
        private TextView bookingId, payable, finishedAt, lblServiceName;

        private MyViewHolder(View view) {
            super(view);

            itemView = view.findViewById(R.id.item_view);
            bookingId = view.findViewById(R.id.booking_id);
            payable = view.findViewById(R.id.payable);
            lblServiceName = view.findViewById(R.id.lblServiceName);
            finishedAt = view.findViewById(R.id.finished_at);
            staticMap = view.findViewById(R.id.static_map);

            itemView.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.redirectClick(list.get(getAdapterPosition()), staticMap);
            });
        }
    }
}