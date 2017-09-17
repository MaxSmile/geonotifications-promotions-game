package com.spindealsapp.database;

import com.spindealsapp.App;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kvm on 29.06.2017.
 */

public class GiftServiceLayer {

    public static HashMap<String, Gift> getGifts(Place place) {
        HashMap<String, Gift> gifts = new HashMap<String, Gift>();
        HashMap<String, Gift> giftsSpin = DBHelper.getInstance(App.getInstance()).getGifts(place.getSpin().getId());
        List<Box> boxes = place.getSpin().getBox();
        if (boxes != null) {
            for (int i = 0; i < boxes.size(); i++) {
                String giftId = boxes.get(i).getGift();
                Gift gift = giftsSpin.get(giftId);
                if (gift != null) {
                    if (gift.getCountAvailable() > 0) {
                        gift.setActive(true);
                    }
                    gifts.put(giftId, gift);
                }
            }
        }

        return gifts;
    }

    public static void insertGift(Gift gift) {
        DBHelper.getInstance(App.getInstance()).insertGift(gift);
    }

    public static void saveGifts(ArrayList<Gift> gifts) {
        DBHelper.getInstance(App.getInstance()).saveGifts(gifts);
    }

}
