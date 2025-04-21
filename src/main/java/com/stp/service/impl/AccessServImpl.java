package com.stp.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.stp.dao.db1.AccessRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.StpAccess;
import com.stp.service.AccessServ;

@Service
@Transactional
public class AccessServImpl implements AccessServ {

	private final AccessRepository accessRepository;

	@Autowired
	public AccessServImpl(AccessRepository accessRepository) {
		this.accessRepository = accessRepository;
	}

	@Override
	@Cacheable(value = "fetchAccessListCache")
	public List<StpAccess> fetchAccessList() {
		// Fetches all active access records
		return accessRepository.findByStatus("A");
	}

	@Override
	@Cacheable(value = "fetchAccessListAllCache")
	public List<StpAccess> fetchAccessListAll() {
		// Fetches all access records, regardless of status
		return accessRepository.findAll();
	}

	@Override
	@Cacheable(value = "addAccessCache")
	public StpAccess addAccessDetail(StpAccess access) {
		// Adds a new access detail record
		return accessRepository.save(access);
	}

	@Override
	@Cacheable(value = "removeAccessLCache")
	public StpAccess removeAccessDetail(String appname) {
		// Retrieves the access detail by appname and marks it as inactive if found
		StpAccess accessDetail = accessRepository.findByAppname(appname);
		if (accessDetail != null) {
			accessDetail.setStatus("N");
			accessDetail.setRemovedate(Date.from(Instant.now()));
			return accessRepository.save(accessDetail);
		} else {
			throw new DetailNotFoundException("Access detail not found for app: " + appname);
		}
	}
}
