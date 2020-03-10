package com.community.hundred.modules.ui.main.fragment.entry;

import java.util.List;

// 主播实体类
public class AnchorEntry {


    private List<ZhuboBean> zhubo;

    public List<ZhuboBean> getZhubo() {
        return zhubo;
    }

    public void setZhubo(List<ZhuboBean> zhubo) {
        this.zhubo = zhubo;
    }

    public static class ZhuboBean {
        /**
         * address : http://zbofang4.brsgmm.cn/live/73633_259148_6859472265.flv
         * img : http://image.huajiao.com/9d34edb5a94bcfe5b44f183fdc1f5f72.jpg
         * title :
         */

        private String address;
        private String img;
        private String title;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "ZhuboBean{" +
                    "address='" + address + '\'' +
                    ", img='" + img + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AnchorEntry{" +
                "zhubo=" + zhubo +
                '}';
    }
}
