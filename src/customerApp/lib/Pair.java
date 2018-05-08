package lib;

public final class Pair<K, V> {
	public K key;
	public V value;
	
	public Pair(K key, V value) {
		if(key == null || value == null) {
			throw new IllegalArgumentException("null key/value not permitted");
		}
		
		this.key = key;
		this.value = value;
	}
}