package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import br.com.ams.sys.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private CacheManager cacheManager;

	@Override
	public void clear(String name) {
		cacheManager.getCache(name).clear();
	}

}
