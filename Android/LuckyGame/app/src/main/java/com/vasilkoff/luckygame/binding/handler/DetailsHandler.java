package com.vasilkoff.luckygame.binding.handler;

import android.view.View;

/**
 * Created by Kvm on 05.06.2017.
 */

public interface DetailsHandler extends GameHandler {
    public void goToPlay(View view);
    public void call(View view);
    public void info(View view);
    public void web(View view);
    public void directions(View view);
    public void moreInfo(View view);
}
