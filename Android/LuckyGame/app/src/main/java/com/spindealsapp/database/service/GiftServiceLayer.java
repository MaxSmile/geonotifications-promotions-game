package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.GiftSqlRepository;
import com.spindealsapp.database.repository.specification.GiftBySpinIdSqlSpecification;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kvm on 29.06.2017.
 */

public class GiftServiceLayer {
    private static GiftSqlRepository repository = new GiftSqlRepository();

    public static HashMap<String, Gift> getGifts(Place place) {
        HashMap<String, Gift> gifts = new HashMap<String, Gift>();
        List<Gift> giftList = repository.query(new GiftBySpinIdSqlSpecification(place.getSpin().getId()));

        HashMap<String, Gift> giftsSpin = new HashMap<String, Gift>();
        for (Gift i : giftList) giftsSpin.put(i.getId(),i);

        List<Box> boxes = place.getSpin().getBox();
        if (boxes != null) {
            for (int i = 0; i < boxes.size(); i++) {
                String giftId = boxes.get(i).getGift();
                Gift gift = giftsSpin.get(giftId);
                if (gift != null) {
                    if (gift.getCountAvailable() > 0) {
                        gift.setActive(true);
                    } else {
                        gift.setActive(false);
                    }
                    gifts.put(giftId, gift);
                }
            }
        }

        return gifts;
    }

    public static void add(Gift gift) {
        repository.add(gift);
    }

    public static void add(Iterable<Gift> gifts) {
        repository.add(gifts);
    }
}
