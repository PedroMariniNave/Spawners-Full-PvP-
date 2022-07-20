package com.zpedroo.voltzspawners.utils.serialization;

import com.zpedroo.voltzspawners.enums.Upgrade;

import java.util.Map;

public class UpgradeSerialization implements ISerialization<Map<Upgrade, Integer>> {

    @Override
    public String serialize(Map<Upgrade, Integer> upgrades) {
        return null;
    }

    @Override
    public Map<Upgrade, Integer> deserialize(String serialized) {
        return null;
    }
}