package com.stp.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.stp.dao.db1.NativeQueryRepository;
import com.stp.dao.db1.TTUMQueryRepository;
import com.stp.dao.db1.TTUMReportQueryRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.STP_REPORTQUERY;
import com.stp.model.db1.STP_TTUMQuery;
import com.stp.service.NativeServ;
import com.stp.utility.TTumRequest;

@Service
public class NativeServImpl implements NativeServ {

	private static final Logger logger = LoggerFactory.getLogger(NativeServImpl.class);

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private TTUMQueryRepository queryRepository;

	@Autowired
	private TTUMReportQueryRepository ttumReportQueryRepository;

	@Override
	@Cacheable(value = "ttumQueryCache")
	public List<STP_TTUMQuery> fetchTTumQuery() {
		List<STP_TTUMQuery> findAllQuery = queryRepository.findAll();
		if (findAllQuery.isEmpty()) {
			logger.error("Query Detail Not Found: No queries available");
			throw new DetailNotFoundException("Query Detail Not Found");
		}
		return findAllQuery;
	}

	@Override
//	@Cacheable(value = "ttumNativeQueryCache", key = "#ttum.queryid + #ttum.type")
	public List<Object[]> executeNativeQuery(TTumRequest ttum) {
		System.out.println("getFromdate" + ttum.getFromdate());
		System.out.println("getTodate" + ttum.getTodate());
		System.out.println("getQueryid" + ttum.getQueryid());
		System.out.println("getType" + ttum.getType());
		System.out.println("getStatus" + ttum.getStatus());
		System.out.println("getPagination" + ttum.getPagination());
		List<STP_TTUMQuery> findAllQuery = queryRepository.findAll();
		Optional<STP_TTUMQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			STP_TTUMQuery stpquery = optionalResult.get();
			logger.debug("Executing native query with query: {}", stpquery);
			return nativeQueryRepository.executeNativeQuery(stpquery, ttum);
		} else {
			logger.error("TTUM not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM not found for : " + ttum.getQueryDetail());
		}
	}

	@Override
	public List<Object[]> executeNativeQueryCount(TTumRequest ttum) {

		System.out.println("getFromdate" + ttum.getFromdate());
		System.out.println("getTodate" + ttum.getTodate());
		System.out.println("getQueryid" + ttum.getQueryid());
		System.out.println("getType" + ttum.getType());
		System.out.println("getStatus" + ttum.getStatus());
		System.out.println("getPagination" + ttum.getPagination());
		
		List<STP_TTUMQuery> findAllQuery = queryRepository.findAll();
		Optional<STP_TTUMQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			STP_TTUMQuery stpquery = optionalResult.get();
			logger.debug("Executing native query count with query: {}", stpquery);
			return nativeQueryRepository.executeNativeQueryCount(stpquery, ttum);
		} else {
			logger.error("TTUM Count not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM count not found for : " + ttum.getQueryDetail());
		}

	}

	@Override
	public List<STP_REPORTQUERY> fetchReport() {
		List<STP_REPORTQUERY> findAllQuery = ttumReportQueryRepository.findAll();
		if (findAllQuery.isEmpty()) {
			logger.error("Query Detail Not Found: No queries available");
			throw new DetailNotFoundException("Query Detail Not Found");
		}
		return findAllQuery;
	}

	@Override
	public byte[] STP_REPORTQUERY(HttpServletRequest request, HttpServletResponse response, TTumRequest ttum) {
		try {
			String fromdate = ttum.getFromdate();
			String todate = ttum.getTodate();
			Integer queryid = ttum.getQueryid();
			List<STP_REPORTQUERY> findAll = ttumReportQueryRepository.findAll();
			List<STP_REPORTQUERY> queryBean = findAll.stream().filter(c -> c.getId() == queryid)
					.collect(Collectors.toList());
			List<String> Header = null;
			findAll = null;
			String query = queryBean.get(0).getQuery();
			String queryheader = queryBean.get(0).getQueryheader();
			System.out.print(" fromdate" + fromdate + " todate:: " + todate + " queryid " + queryid);
			logger.error(" fromdate" + fromdate + " todate:: " + todate + " queryid " + queryid);
			logger.error(" queryheader :::" + queryheader);
			Header = new ArrayList<String>(Arrays.asList(queryheader.split(",")));
			List<Object[]> obj = nativeQueryRepository.getSTP_REPORTQUERY(query, ttum);
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook();
				CellStyle style = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setFontName("Arial");
				font.setColor(IndexedColors.BLACK.getIndex());
				style.setFont(font);
				CellStyle styleHeader = workbook.createCellStyle();
				styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				Font fontheader = workbook.createFont();
				fontheader.setFontName("Arial");
				styleHeader.setFont(fontheader);
				Sheet sh1 = workbook.createSheet("" + queryBean.get(0).getSubtype());
				Iterator itr = obj.iterator();
				int rowCount = 1;
				int column = 0;
				Row rowHeader1 = sh1.createRow(0);
				for (int i = 0; i < Header.size(); i++) {
					rowHeader1.createCell(i).setCellValue(Header.get(i));
					rowHeader1.getCell(i).setCellStyle(styleHeader);
				}
				Header = null;
				if (obj.size() > 0) {
					while (itr.hasNext()) {
						Row rowHeader = sh1.createRow(rowCount++);
						Object[] object = (Object[]) itr.next();
						int count = 0;
						for (int i = 0; i < object.length; i++) {
							rowHeader.createCell(count)
									.setCellValue(object[i] != null ? (String) object[i].toString() : "");
							rowHeader.getCell(count).setCellStyle(style);
							count++;
						}
						itr.remove();
					}
				}
				obj = null;

				// end create excel
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Write the workbook to a byte array output stream
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			workbook.write(byteArrayOutputStream);
			workbook.close();
			System.out.println("compelete excel download");
			return byteArrayOutputStream.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public List<Object[]> STP_REPORTQUERYDatatable(HttpServletRequest request, HttpServletResponse response,
			TTumRequest ttum) {
		List<Object[]> obj = null;
		StringBuilder sb = new StringBuilder();
		String fromdate = ttum.getFromdate();
		String todate = ttum.getTodate();
		Integer queryid = ttum.getQueryid();
		List<STP_REPORTQUERY> findAll = ttumReportQueryRepository.findAll();
		List<STP_REPORTQUERY> queryBean = findAll.stream().filter(c -> c.getId() == queryid)
				.collect(Collectors.toList());
		List<String> Header = null;
		findAll = null;
		String query = queryBean.get(0).getQuery();
		logger.info(" query" + query);
		obj = nativeQueryRepository.getSTP_REPORTQUERY(query, ttum);
		return obj;
	}

}
