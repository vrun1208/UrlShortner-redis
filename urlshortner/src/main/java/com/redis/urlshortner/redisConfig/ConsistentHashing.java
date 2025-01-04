package com.redis.urlshortner.redisConfig;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

public class ConsistentHashing {
	// for manual ordering according to there keys
	private final SortedMap<Integer, LettuceConnectionFactory> ring = new TreeMap<>();
	
	private int hash(String key) {
		return key.hashCode() & 0x7fffffff;
	}
	
	public ConsistentHashing(List<LettuceConnectionFactory> factories) {
		for(int i=0; i< factories.size(); i++) {
			ring.put(hash("node - "+ i), factories.get(i)); // each node (redis instance) assigned a hashvalue on its identifier
		}
	}
	
//	we have 3 Redis nodes (node-0, node-1, node-2) with hash values:
//
//		node-0 → 123
//		node-1 → 456
//		node-2 → 789
	
//		
//	When a key like "www.kuchbhi.com" is hashed to 500:
//
//		500 is greater than 456 (hash of node-1) but less than 789 (hash of node-2).
//		The key is stored in node-2.
	
	
	
	public LettuceConnectionFactory getFactory(String key) {
		if (ring.isEmpty()) {
            return null;
        }
        int hash = hash(key);
        if (!ring.containsKey(hash)) {
            return ring.tailMap(hash).isEmpty() ? ring.get(ring.firstKey()) : ring.tailMap(hash).get(ring.tailMap(hash).firstKey());
        }
        return ring.get(hash);
    }
	
}
