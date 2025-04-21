package com.stp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stp.model.db1.StpReportQuery;
import com.stp.model.db1.StpTtumQuery;
import com.stp.utility.StpProcessRequest;
import com.stp.utility.TTumRequest;

public interface NativeServ {

	public List<Object[]> executeNativeQuery(TTumRequest ttum);

	public List<StpTtumQuery> fetchTTumQuery();

	public List<Object[]> executeNativeQueryCount(TTumRequest ttum);

	// report query
	public List<StpReportQuery> fetchReport();

	public byte[] stpReportQuery(HttpServletRequest request, HttpServletResponse response, TTumRequest ttum)
			throws IOException;

	public List<Object[]> stpReportQueryDatatable(HttpServletRequest request, HttpServletResponse response,
			TTumRequest ttum);

	List<Object[]> executeNativeQueryCountBatch(TTumRequest ttum);

	public List<Object[]> executeNativeQueryBatch(TTumRequest ttum);

	public int processLevelStp(List<StpProcessRequest> json, String type, String level, String process);

}
