package com.vasilkoff.luckygame.direction;

import java.util.List;

/**
 * Created by Kvm on 15.06.2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
