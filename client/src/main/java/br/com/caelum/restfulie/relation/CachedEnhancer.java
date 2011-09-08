package br.com.caelum.restfulie.relation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Caires & Thiago Miranda
 * This cached implementation will solve 99% of the concurrency problems,
 * but it still may have some cases of concurrency.
 */

@SuppressWarnings("rawtypes")
public class CachedEnhancer implements Enhancer {

	private final Enhancer enhancer;
	private Map<Class, Class> cache = new HashMap<Class, Class>();
	private final Lock lock = new ReentrantLock();

	public CachedEnhancer(Enhancer enhancer) {
		this.enhancer = enhancer;
	}

	public <T> Class enhanceResource(Class<T> originalType) {
		if(cache.containsKey(originalType)) {
			return cache.get(originalType);
		}
		lock.lock();
		try {
			if(cache.containsKey(originalType)) {
				return cache.get(originalType);
			}
			Class enhanced = enhancer.enhanceResource(originalType);
			cache.put(originalType, enhanced);
			return enhanced;
		} finally {
			lock.unlock();
		}
	}

}
