package com.vasilkoff.luckygame.database;

import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kvm on 29.06.2017.
 */

public class GiftServiceLayer {

    public static HashMap<String, Gift> getGifts(Place place) {
        HashMap<String, Gift> gifts = new HashMap<String, Gift>();
        HashMap<String, Gift> giftsCompany = DBHelper.getInstance(App.getInstance()).getGifts(place.getCompanyKey());
        List<Box> boxes = place.getBox();
        for (int i = 0; i < boxes.size(); i++) {
            String giftId = boxes.get(i).getGift();
            Gift gift = giftsCompany.get(giftId);
            if (gift != null) {
                if (gift.isActive()) {
                    if (!(gift.getDateStart() < System.currentTimeMillis()
                            && gift.getDateFinish() > System.currentTimeMillis())) {
                        gift.setActive(false);
                    }
                }

                gifts.put(giftId, giftsCompany.get(giftId));
            }
        }
        return gifts;
    }
}
