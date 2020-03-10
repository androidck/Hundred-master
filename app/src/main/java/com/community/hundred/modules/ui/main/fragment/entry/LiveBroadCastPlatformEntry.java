package com.community.hundred.modules.ui.main.fragment.entry;

import java.util.List;

public class LiveBroadCastPlatformEntry {


    private List<PingtaiBean> pingtai;

    public List<PingtaiBean> getPingtai() {
        return pingtai;
    }

    public void setPingtai(List<PingtaiBean> pingtai) {
        this.pingtai = pingtai;
    }

    public static class PingtaiBean {
        /**
         * address : jsonhonggaoliang.txt
         * xinimg : http://t.lzpeng.com/data/upload/20191201/5de3d453e62c8.jpg
         * Number : 140
         * title : 红高粱
         */

        private String address;
        private String xinimg;
        private String Number;
        private String title;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getXinimg() {
            return xinimg;
        }

        public void setXinimg(String xinimg) {
            this.xinimg = xinimg;
        }

        public String getNumber() {
            return Number;
        }

        public void setNumber(String Number) {
            this.Number = Number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "PingtaiBean{" +
                    "address='" + address + '\'' +
                    ", xinimg='" + xinimg + '\'' +
                    ", Number='" + Number + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LiveBroadCastPlatformEntry{" +
                "pingtai=" + pingtai +
                '}';
    }
}
