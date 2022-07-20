package com.zpedroo.voltzspawners.utils.serialization;

public interface ISerialization<T> {

    String serialize(T deserialized);

    T deserialize(String serialized);
}