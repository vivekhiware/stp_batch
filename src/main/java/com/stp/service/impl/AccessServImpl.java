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
import com.stp.model.db1.STP_Access;
import com.stp.service.AccessServ;

@Service
@Transactional
public class AccessServImpl implements AccessServ {

	@Autowired
	private AccessRepository accessRepository;

	@Override
	@Cacheable(value = "fetchAccessListCache")
	public List<STP_Access> fetchAccessList() {
		// Fetches all active access records
		return accessRepository.findByStatus("A");
	}

	@Override
	@Cacheable(value = "fetchAccessListAllCache")
	public List<STP_Access> fetchAccessListAll() {
		// Fetches all access records, regardless of status
		return accessRepository.findAll();
	}

	@Override
	@Cacheable(value = "addAccessCache")
	public STP_Access addAccessDetail(STP_Access access) {
		// Adds a new access detail record
		return accessRepository.save(access);
	}

	@Override
	@Cacheable(value = "removeAccessLCache")
	public STP_Access removeAccessDetail(String appname) {
		// Retrieves the access detail by appname and marks it as inactive if found
		STP_Access accessDetail = accessRepository.findByAppname(appname);
		if (accessDetail != null) {
			accessDetail.setStatus("N");
			accessDetail.setRemovedate(Date.from(Instant.now()));
			return accessRepository.save(accessDetail);
		} else {
			throw new DetailNotFoundException("Access detail not found for app: " + appname);
		}
	}
}
