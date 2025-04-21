package com.stp.service.impl;

import java.io.ByteArrayOutputStream;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.stp.dao.db1.NativeQueryRepository;
import com.stp.dao.db1.TTUMQueryRepository;
import com.stp.dao.db1.TTUMReportQueryRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.StpReportQuery;
import com.stp.model.db1.StpTtumQuery;
import com.stp.service.NativeServ;
import com.stp.utility.StpProcessRequest;
import com.stp.utility.TTumRequest;

@Service
public class NativeServImpl implements NativeServ {
	private static final String TTUM_REQUEST_LOG = "TTUM Request - fromdate: {}, todate: {}, queryid: {}, type: {}, status: {}, pagination: {}";

	private static final Logger logger = LoggerFactory.getLogger(NativeServImpl.class);

	private final NativeQueryRepository nativeQueryRepository;
	private final TTUMQueryRepository queryRepository;
	private final TTUMReportQueryRepository ttumReportQueryRepository;

	@Autowired
	public NativeServImpl(NativeQueryRepository nativeQueryRepository, TTUMQueryRepository queryRepository,
			TTUMReportQueryRepository ttumReportQueryRepository) {
		this.nativeQueryRepository = nativeQueryRepository;
		this.queryRepository = queryRepository;
		this.ttumReportQueryRepository = ttumReportQueryRepository;
	}

	@Override
	@Cacheable(value = "ttumQueryCache")
	public List<StpTtumQuery> fetchTTumQuery() {
		List<StpTtumQuery> findAllQuery = queryRepository.findAll();
		if (findAllQuery.isEmpty()) {
			logger.error("Query Detail Not Found: No queries available");
			throw new DetailNotFoundException("Query Detail Not Found");
		}
		return findAllQuery;
	}

	@Override
	public List<Object[]> executeNativeQueryCount(TTumRequest ttum) {
		logger.info(TTUM_REQUEST_LOG, ttum.getFromdate(), ttum.getTodate(), ttum.getQueryid(), ttum.getType(),
				ttum.getStatus(), ttum.getPagination());

		List<StpTtumQuery> findAllQuery = queryRepository.findAll();
		Optional<StpTtumQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			StpTtumQuery stpquery = optionalResult.get();
			logger.debug("Executing native query count with query: {}", stpquery);
			return nativeQueryRepository.executeNativeQueryCountForBatch(stpquery, ttum);
		} else {
			logger.error("TTUM Count not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM count not found for : " + ttum.getQueryDetail());
		}

	}

	@Override
//	@Cacheable(value = "ttumNativeQueryCache", key = "#ttum.queryid + #ttum.type")
	public List<Object[]> executeNativeQuery(TTumRequest ttum) {
		logger.info(TTUM_REQUEST_LOG, ttum.getFromdate(), ttum.getTodate(), ttum.getQueryid(), ttum.getType(),
				ttum.getStatus(), ttum.getPagination());

		List<StpTtumQuery> findAllQuery = queryRepository.findAll();
		Optional<StpTtumQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			StpTtumQuery stpquery = optionalResult.get();
			logger.debug("Executing native query with query: {}", stpquery);

			return nativeQueryRepository.executeNativeQuery(stpquery, ttum);
		} else {
			logger.error("TTUM not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM not found for : " + ttum.getQueryDetail());
		}
	}

	@Override
	public List<StpReportQuery> fetchReport() {
		List<StpReportQuery> findAllQuery = ttumReportQueryRepository.findAll();
		if (findAllQuery.isEmpty()) {
			logger.error("Query Detail Not Found: No queries available");
			throw new DetailNotFoundException("Query Detail Not Found");
		}
		return findAllQuery;
	}

	@Override
	public byte[] stpReportQuery(HttpServletRequest request, HttpServletResponse response, TTumRequest ttum) {

		String fromdate = ttum.getFromdate();
		String todate = ttum.getTodate();
		Integer queryid = ttum.getQueryid();
		List<StpReportQuery> findAll = ttumReportQueryRepository.findAll();
		List<StpReportQuery> queryBean = findAll.stream().filter(c -> c.getId() == queryid)
				.collect(Collectors.toList());
		List<String> header = null;
		findAll = null;

		String query = queryBean.get(0).getQuery();
		String queryheader = queryBean.get(0).getQueryheader();

		logger.info("TTUM Report Request - fromdate: {}, todate: {}, queryid: {}", fromdate, todate, queryid);
		logger.debug("Query Header: {}", queryheader);

		header = new ArrayList<>(Arrays.asList(queryheader.split(",")));
		List<Object[]> obj = nativeQueryRepository.getStpReportquery(query, ttum);

		try (Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

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

			Sheet sh1 = workbook.createSheet(queryBean.get(0).getSubtype());
			Iterator<Object[]> itr = obj.iterator();
			int rowCount = 1;

			Row rowHeader1 = sh1.createRow(0);
			for (int i = 0; i < header.size(); i++) {
				rowHeader1.createCell(i).setCellValue(header.get(i));
				rowHeader1.getCell(i).setCellStyle(styleHeader);
			}
			header = null;

			if (!obj.isEmpty()) {
				while (itr.hasNext()) {
					Row rowHeader = sh1.createRow(rowCount++);
					Object[] object = itr.next();
					int count = 0;
					for (int i = 0; i < object.length; i++) {
						rowHeader.createCell(count).setCellValue(object[i] != null ? object[i].toString() : "");
						rowHeader.getCell(count).setCellStyle(style);
						count++;
					}
					itr.remove();
				}
			}
			obj = null;

			workbook.write(byteArrayOutputStream);
			logger.info("Complete Excel download for subtype: {}", queryBean.get(0).getSubtype());
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			logger.error("Error while generating Excel: ", e);
		}
		return new byte[1];

	}

	@Override
	public List<Object[]> stpReportQueryDatatable(HttpServletRequest request, HttpServletResponse response,
			TTumRequest ttum) {
		List<Object[]> obj = null;
		Integer queryid = ttum.getQueryid();
		List<StpReportQuery> findAll = ttumReportQueryRepository.findAll();
		List<StpReportQuery> queryBean = findAll.stream().filter(c -> c.getId() == queryid)
				.collect(Collectors.toList());

		String query = queryBean.get(0).getQuery();
		logger.debug("Query for DataTable: {}", query);
		obj = nativeQueryRepository.getStpReportquery(query, ttum);
		return obj;
	}

	@Override
	public List<Object[]> executeNativeQueryCountBatch(TTumRequest ttum) {
		logger.info(TTUM_REQUEST_LOG, ttum.getFromdate(), ttum.getTodate(), ttum.getQueryid(), ttum.getType(),
				ttum.getStatus(), ttum.getPagination());

		List<StpTtumQuery> findAllQuery = queryRepository.findAll();
		Optional<StpTtumQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			nativeQueryRepository.callBatchCreationIsoTest(ttum.getQueryid(), ttum.getFromdate(), ttum.getType());
			StpTtumQuery stpquery = optionalResult.get();
			logger.debug("Executing native query count with query: {}", stpquery);
			return nativeQueryRepository.executeNativeQueryCountForBatch(stpquery, ttum);
		} else {
			logger.error("TTUM Count not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM count not found for : " + ttum.getQueryDetail());
		}

	}

	public List<Object[]> executeNativeQueryBatch(TTumRequest ttum) {
		logger.info(TTUM_REQUEST_LOG, ttum.getFromdate(), ttum.getTodate(), ttum.getQueryid(), ttum.getType(),
				ttum.getStatus(), ttum.getPagination());

		List<StpTtumQuery> findAllQuery = queryRepository.findAll();
		Optional<StpTtumQuery> optionalResult = findAllQuery.stream().filter(
				entity -> entity.getId() == ttum.getQueryid() && entity.getType().equalsIgnoreCase(ttum.getType()))
				.findFirst();
		if (optionalResult.isPresent()) {
			StpTtumQuery stpquery = optionalResult.get();
			logger.debug("Executing native query with query: {}", stpquery);
			return nativeQueryRepository.executeNativeQueryBatch(stpquery, ttum);
		} else {
			logger.error("TTUM not found for: {}", ttum.getQueryDetail());
			throw new DetailNotFoundException("TTUM not found for : " + ttum.getQueryDetail());
		}
	}

	@Override
	public int processLevelStp(List<StpProcessRequest> json, String type, String level, String process) {
		Integer queryid = Integer.parseInt(type);
		StpTtumQuery stpquery = queryRepository.findById(queryid)
				.orElseThrow(() -> new DetailNotFoundException("processLevelStp not found"));
		return nativeQueryRepository.processLevelStp(json, stpquery.getQuerytbl(), type, level, process);
	}

}
