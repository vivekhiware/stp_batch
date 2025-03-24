package com.stp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stp.model.db1.STP_REPORTQUERY;
import com.stp.model.db1.STP_TTUMQuery;
import com.stp.utility.TTumRequest;

public interface NativeServ {

	public List<Object[]> executeNativeQuery(TTumRequest ttum);

	public List<STP_TTUMQuery> fetchTTumQuery();

	public List<Object[]> executeNativeQueryCount(TTumRequest ttum);

	// report query
	public List<STP_REPORTQUERY> fetchReport();

	public byte[] STP_REPORTQUERY(HttpServletRequest request, HttpServletResponse response, TTumRequest ttum)
			throws IOException;

	public List<Object[]> STP_REPORTQUERYDatatable(HttpServletRequest request, HttpServletResponse response,
			TTumRequest ttum);
}
