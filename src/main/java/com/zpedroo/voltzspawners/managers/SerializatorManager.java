package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import com.zpedroo.voltzspawners.utils.serialization.UpgradeSerialization;

public class SerializatorManager {

    private static final LocationSerialization locationSerialization = new LocationSerialization();
    private static final UpgradeSerialization upgradeSerialization = new UpgradeSerialization();

    public static LocationSerialization getLocationSerialization() {
        return locationSerialization;
    }

    public static UpgradeSerialization getUpgradeSerialization() {
        return upgradeSerialization;
    }
}