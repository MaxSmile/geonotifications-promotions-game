package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.GallerySqlRepository;
import com.spindealsapp.database.repository.specification.GalleryByOwnerSqlSpecification;
import com.spindealsapp.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class GalleryServiceLayer {

    private static GallerySqlRepository repository = new GallerySqlRepository();

    public static void add(Iterable<Gallery> gallery) {
        repository.add(gallery);
    }

    public static List<String> getGallery(String owner) {
        List<String> gallery = new ArrayList<String>();
        List<Gallery> list = repository.query(new GalleryByOwnerSqlSpecification(owner));
        for (int i = 0; i < list.size(); i++) {
            gallery.add(list.get(i).getPath());
        }
        return gallery;
    }
}
