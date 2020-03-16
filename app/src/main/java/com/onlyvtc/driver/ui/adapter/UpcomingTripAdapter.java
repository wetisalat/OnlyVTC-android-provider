package com.onlyvtc.driver.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.onlyvtc.driver.BuildConfig;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.data.network.model.HistoryList;
import com.onlyvtc.driver.data.network.model.WayPoint;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpcomingTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryList> list;
    private Context context;

    private ClickListener clickListener;

    public UpcomingTripAdapter(List<HistoryList> list, Context con) {
        this.list = list;
        this.context = con;
    }

    public void setList(List<HistoryList> list) {
        this.list = list;
    }

    public void setClickListener(UpcomingTripAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_upcoming_trip,
                            parent, false);
            return new MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_upcoming_trip2,
                            parent, false);
            return new MyViewHolder2(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HistoryList item = list.get(position);

        if (holder.getItemViewType() == 0) {
            MyViewHolder holder0 = (MyViewHolder) holder;
            String strCurrentDate = item.getScheduleAt();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat timeFormat;
            Date newDate;
            try {
                newDate = format.parse(strCurrentDate);
                format = new SimpleDateFormat("dd-MM-yyyy");
                timeFormat = new SimpleDateFormat("HH:mm:ss");
                String date = format.format(newDate);
                String time = timeFormat.format(newDate);
                holder0.lblDate.setText(date + " " + time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder0.lblBookingid.setText(item.getBookingId());
            holder0.lblSeviceName.setText(item.getServicetype().getName());

            Glide.with(context).load(item.getStaticMap()).
                    apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                            dontAnimate().error(R.drawable.ic_launcher_background).
                            diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder0.staticMap);
        } else {
            MyViewHolder2 holder2 = (MyViewHolder2) holder;
            try {
                holder2.lblUserName.setText(item.getUser().getFirstName() + " " + item.getUser().getLastName());
                holder2.ratingUser.setRating(Float.valueOf(item.getUser().getRating()));
                holder2.tvPickup.setText(item.getSAddress());
                holder2.tvDropOff.setText(item.getDAddress());
                Glide.with(context).load(BuildConfig.BASE_IMAGE_URL + item.getUser().getPicture()).
                        apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                                dontAnimate().error(R.drawable.ic_user_placeholder).
                                diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder2.imgUser);

                Glide.with(context).load(item.getStaticMap()).
                        apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                dontAnimate().error(R.drawable.ic_launcher_background).
                                diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder2.staticMap);

                holder2.countdownview = new CountdownView(context);
                holder2.countdownview.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                holder2.countdownview.setOnCountdownIntervalListener(1, new CountdownView.OnCountdownIntervalListener() {
                    @Override
                    public void onInterval(CountdownView cv, long remainTimes) {
                        long remainTime = remainTimes / 1000;
                        long day = remainTime / (60 * 60 * 24);
                        long hour = remainTime % (60 * 60 * 24) / (60 * 60);
                        long min = remainTime % (60 * 60) / 60;
                        long second = remainTime % 60;
                        holder2.lblCount.setText(String.format("%02d:%02d:%02d", (day * 24) + hour, min, second));

                    }
                });

                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sourceFormat.setTimeZone(TimeZone.getTimeZone(item.getTimezone()));
                Date parsed = sourceFormat.parse(item.getManual_assigned_at());
                Calendar cal = Calendar.getInstance();
                cal.setTime(parsed);
                cal.add(Calendar.HOUR, item.getTimeout());

                holder2.countdownview.start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());

                WayPoint waypoint1 = null;
                String wayPointString1 = item.getWay_points();
                try {
                    JSONArray ary = new JSONArray(wayPointString1);
                    if (ary.length() > 0) {
                        waypoint1 = new WayPoint(ary.optJSONObject(0));
                    }
                } catch (Exception e) {
                    Log.d("tag", e.toString());
                }
                if (waypoint1 != null) {
                    holder2.tvStopLocation.setText(waypoint1.getAddress() == null ? "" : waypoint1.getAddress());
                } else {
                    holder2.container_stop.setVisibility(View.GONE);
                }

                String strCurrentDate = item.getScheduleAt();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
                SimpleDateFormat timeFormat;
                Date newDate;
                try {
                    newDate = format.parse(strCurrentDate);
                    format = new SimpleDateFormat("dd-MM-yyyy");
                    timeFormat = new SimpleDateFormat("HH:mm:ss");
                    String date = format.format(newDate);
                    String time = timeFormat.format(newDate);
                    holder2.scheduled_at.setText(date + " " + time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getManual_assigned_at() == null ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickListener {
        void redirectClick(HistoryList item, ImageView staticMap);

        void cancelRide(HistoryList item, int pos);

        void acceptRide(HistoryList item, int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView itemView;
        private TextView lblDate, lblBookingid, lblSeviceName;
        private ImageView staticMap;
        private Button btnCancelRide;

        private MyViewHolder(View view) {
            super(view);

            itemView = view.findViewById(R.id.item_view);
            lblDate = view.findViewById(R.id.lblDate);
            lblBookingid = view.findViewById(R.id.lblBookingid);
            lblSeviceName = view.findViewById(R.id.lblSeviceName);
            staticMap = view.findViewById(R.id.static_map);

            btnCancelRide = view.findViewById(R.id.btnCancelRide);

            itemView.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.redirectClick(list.get(getAdapterPosition()), staticMap);
            });

            btnCancelRide.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.cancelRide(list.get(getAdapterPosition()), getAdapterPosition());
            });
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {

        private CardView itemView;
        private ImageView staticMap;
        private TextView lblCount;
        private TextView lblUserName;
        private AppCompatRatingBar ratingUser;
        private CircleImageView imgUser;
        private TextView tvDropOff, tvPickup;
        private Button btnReject, btnAccept;
        private LinearLayout container_stop;
        private TextView tvStopLocation;
        private CountdownView countdownview;
        private TextView scheduled_at;

        private MyViewHolder2(View view) {
            super(view);

            itemView = view.findViewById(R.id.item_view);
            staticMap = view.findViewById(R.id.staticMap);
            btnReject = view.findViewById(R.id.btnReject);
            btnAccept = view.findViewById(R.id.btnAccept);
            tvDropOff = view.findViewById(R.id.tvDropOff);
            tvPickup = view.findViewById(R.id.tvPickup);
            imgUser = view.findViewById(R.id.imgUser);
            ratingUser = view.findViewById(R.id.ratingUser);
            lblCount = view.findViewById(R.id.lblCount);
            lblUserName = view.findViewById(R.id.lblUserName);
            container_stop = view.findViewById(R.id.container_stop);
            tvStopLocation = view.findViewById(R.id.tvStopLocation);
            scheduled_at = view.findViewById(R.id.schedule_at);

            itemView.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.redirectClick(list.get(getAdapterPosition()), staticMap);
            });

            btnReject.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.cancelRide(list.get(getAdapterPosition()), getAdapterPosition());
            });

            btnAccept.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.acceptRide(list.get(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}