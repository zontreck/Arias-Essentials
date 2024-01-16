package dev.zontreck.essentials.util;

import java.util.HashMap;
import java.util.Map;

public class Maps <K,V>
{
    public Map<K,V> map = new HashMap<>();

    public static <K,V> Maps<K,V> builder(K k, V v)
    {
        return new Maps<K,V>();
    }

    public Maps<K,V> with(K k, V v)
    {
        map.put(k,v);
        return this;
    }

    public Map<K,V> build()
    {
        return map;
    }


}
