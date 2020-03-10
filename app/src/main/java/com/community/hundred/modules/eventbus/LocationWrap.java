package com.community.hundred.modules.eventbus;

import com.amap.api.location.AMapLocation;

// 地址发送
public class LocationWrap {

    public final AMapLocation location;

    private LocationWrap(AMapLocation location) {
        this.location = location;
    }

    public static LocationWrap getInstance(AMapLocation location) {
        return new LocationWrap(location);
    }
}
