package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.NotificationSqlRepository;
import com.spindealsapp.database.repository.specification.NotificationByIdSqlSpecification;
import com.spindealsapp.entity.PlaceNotification;

import java.util.List;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class NotificationServiceLayer {

    private static NotificationSqlRepository repository = new NotificationSqlRepository();

    public static void add(PlaceNotification notification) {
        repository.add(notification);
    }

    public static long getTimeNotification(String placeId) {
        List<PlaceNotification> placeNotifications = repository.query(new NotificationByIdSqlSpecification(placeId));
        if (placeNotifications.size() > 0) {
            return placeNotifications.get(0).getLastNotification();
        } else {
            return 0;
        }
    }
}
