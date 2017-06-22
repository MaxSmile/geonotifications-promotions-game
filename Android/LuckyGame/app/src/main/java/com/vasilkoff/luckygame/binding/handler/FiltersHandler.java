package com.vasilkoff.luckygame.binding.handler;

import android.view.View;

/**
 * Created by Kvm on 22.06.2017.
 */

public interface FiltersHandler extends BaseHandler {
    public void filterNearMe(View view);
    public void filters(View view);
    public void search(View view);
    public void hideSearch(View view);
    public void offSearch(View view);

}
