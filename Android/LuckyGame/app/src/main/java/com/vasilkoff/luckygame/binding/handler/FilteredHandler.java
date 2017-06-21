package com.vasilkoff.luckygame.binding.handler;

import android.view.View;

/**
 * Created by Kvm on 21.06.2017.
 */

public interface FilteredHandler extends BaseHandler {
    public void filterNearMe(View view);
    public void filters(View view);
    public void search(View view);
}
