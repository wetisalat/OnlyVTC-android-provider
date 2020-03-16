package com.onlyvtc.driver.ui.fragment.upcoming;


import com.onlyvtc.driver.base.MvpPresenter;

import java.util.HashMap;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

    void assignedCancel(HashMap<String, Object> map);

    void assignedAccepted(HashMap<String, Object> map);
}
