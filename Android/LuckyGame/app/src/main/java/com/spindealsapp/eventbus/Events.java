package com.spindealsapp.eventbus;

/**
 * Created by Kvm on 16.06.2017.
 */

public class Events {

    public static class UpdateLocation {
    }

    public static class UpdateFilter {
    }

    public static class UpdateCoupons {
    }

    public static class UpdatePlaces {
    }

    public static class AddedCoupon {
    }

    public static class FinishLoadData {
    }

    public static class FinishCalculateData {
    }

    public static class LoadingData {
        private int percent;

        public LoadingData(int percent) {
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }
    }

    public static class CheckCoupons {
        private boolean exist;

        public CheckCoupons(boolean exist) {
            this.exist = exist;
        }

        public boolean isExist() {
            return exist;
        }
    }
}