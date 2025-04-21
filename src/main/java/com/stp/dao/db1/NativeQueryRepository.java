package com.stp.dao.db1;

import static com.stp.utility.GenericCLass.convertStringToSqlDate;
import static com.stp.utility.GenericCLass.getStatusList;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.iso.config.IsoV93Message;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.exception.CustomDatabaseException;
import com.stp.model.db1.StpProcessDetail;
import com.stp.model.db1.StpTtumQuery;
import com.stp.utility.StpProcessRequest;
import com.stp.utility.TTumRequest;

@Repository
@Transactional
public class NativeQueryRepository {
	private static final String ERRORRESPONSE = "Error executing query";
	private static final Logger logger = LoggerFactory.getLogger(NativeQueryRepository.class);

	@PersistenceContext
	private EntityManager entityManager;

	// Execute a native query and return results
	public List<Object[]> executeNativeQuery(StpTtumQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQuery());
		query.append(" LIMIT ");
		query.append(req.getPagination());
		query.append(" OFFSET 0");
		logger.info("query :{}", query);
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public List<Object[]> executeNativeQueryCount(StpTtumQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQueryforCount());
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public List<Object[]> getStpReportquery(String queryDetail, TTumRequest req) {
		StringBuilder query = new StringBuilder(queryDetail);
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	// Execute a native query and return results
	public List<Object[]> executeNativeQueryForBatch(StpTtumQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQuery());
		logger.info("Executing batch query: {}", query);

		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
		} catch (IllegalArgumentException e) {

			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}

		try {
			return nativeQuery.getResultList();
		} catch (Exception e) {

			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public List<Object[]> executeNativeQueryCountForBatch(StpTtumQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQueryforCount());
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public void callBatchCreationIsoTest(int queryid, String processDate, String type) {
		String proc = "NA";
		if (type.equalsIgnoreCase("IMPS")) {
			proc = "BatchCreationIsoImps";
		} else if (type.equalsIgnoreCase("UPI")) {
			proc = "BatchCreationIsoUpi";
		} else if (type.equalsIgnoreCase("CARDS")) {
			proc = "BatchCreationIsoCards";
		} else if (type.equalsIgnoreCase("ATM")) {
			proc = "BatchCreationIsoAtm";
		}
		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(proc);
		procedureQuery.registerStoredProcedureParameter("queryid", Integer.class, ParameterMode.IN);
		procedureQuery.registerStoredProcedureParameter("process_date", String.class, ParameterMode.IN);
		procedureQuery.setParameter("queryid", queryid);
		procedureQuery.setParameter("process_date", processDate);
		procedureQuery.execute();
	}

	public List<Object[]> executeNativeQueryBatch(StpTtumQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQuery());
		logger.info("Executing batch query: {}", query);
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", getStatusList(req.getStatus()));
		} catch (IllegalArgumentException e) {

			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
		try {
			return nativeQuery.getResultList();
		} catch (Exception e) {

			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public String createDynamicQuery(String q1, String level, String process) {
		StringBuilder sb = new StringBuilder("update ");
		sb.append(q1);
		sb.append(" SET status = :x ");
		String updateParameters = createUpdateparameter(level, process);
		if (!updateParameters.isEmpty()) {
			sb.append(", ").append(updateParameters);
		}
		sb.append(" where batchid IN (:y) and DATE_FORMAT(valuedate, '%d-%b-%Y') = :z");
		return sb.toString();
	}

	public String createUpdateparameter(String level, String process) {
		switch (level.toUpperCase() + "_" + process.toUpperCase()) {
		case "R_APPROVE":
			return " makerid=:a , makerdate=:b , makerremarks=:c ";
		case "L0_APPROVE":
			return " makerid=:a , makerdate=:b , makerremarks=:c ";
		case "L0_REJECT":
			return " makeridrej=:a , makerdaterej=:b , makerremarksrej=:c ";
		case "L1_APPROVE":
			return " checkerid=:a , checkerdate=:b , checkerremarks=:c ";
		case "L1_REJECT":
			return " checkeridrej=:a , checkerdaterej=:b , checkerremarksrej=:c ";
		case "L2_APPROVE":
			return " bnkmakerid=:a , bnkmakerdate=:b , bnkmakerremarks=:c ";
		case "L2_REJECT":
			return " bnkmakeridrej=:a , bnkmakerdaterej=:b , bnkmakerremarksrej=:c ";
		case "L3_APPROVE":
			return " bnkcheckerid=:a , bnkcheckerdate=:b , bnkcheckerremarks=:c ";
		case "L3_REJECT":
			return " bnkcheckeridrej=:a , bnkcheckerdaterej=:b , bnkcheckerremarksrej=:c ";
		default:
			return "";
		}
	}

	@Transactional
	public int processLevelStp(List<StpProcessRequest> json, String query, String type, String level, String process) {
		String dynamicQuery = createDynamicQuery(query, level, process);
		logger.info("dynamicQuery: {}", dynamicQuery);

		List<String> batchIds = json.stream().map(StpProcessRequest::getBatchid).collect(Collectors.toList());
		String status = json.get(0).getStatus();
		String valueDate = json.get(0).getValuedate();
		String userId = json.get(0).getUserid();
		Date date = convertStringToSqlDate(json.get(0).getUserdate());
		String remarks = json.get(0).getUserremark();
		int executeUpdate = 0;
		try {
			Query q = entityManager.createNativeQuery(dynamicQuery);
			q.setParameter("x", status);
			q.setParameter("a", userId);
			q.setParameter("b", date);
			q.setParameter("c", remarks);
			q.setParameter("y", batchIds);
			q.setParameter("z", valueDate);
			executeUpdate = q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return executeUpdate;
	}

	public List<Object[]> getBatchDetail(String tablename, String status) {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) AS count, batchid FROM ");
		query.append(tablename);
		query.append(" WHERE status =:a GROUP BY batchid");
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", status);
			return nativeQuery.getResultList();
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

	public String updateDynamicQueryForIso(String q1, String level) {
		StringBuilder sb = new StringBuilder("update ");
		sb.append(q1);
		sb.append(" set  status=:status ");
		String updateParameters = createUpdateparameterIso(level);
		if (!updateParameters.isEmpty()) {
			sb.append(", ").append(updateParameters);
		}
		sb.append(" where refnumber=:ref  ");
		return sb.toString();
	}

	public String createUpdateparameterIso(String level) {
		switch (level.toUpperCase()) {
		case "L4":
			return " res039=:str039 , res125=:str125, res126=:str126 , approvestatus=:approvestatus ";
		case "L5":
			return " res039enq=:str039 , res125enq=:str125, res126enq=:str126 , approvestatus=:approvestatus ";
		case "L6":
			return " res039reinit=:str039 , res125reinit=:str125, res126reinit=:str126 , approvestatus=:approvestatus ";

		default:
			return "";
		}
	}

	@Transactional
	public int processLevelIsoStatusUpdate(String dynamicQueryForUpdate, IsoV93Message isoMsg) {
		int executeUpdate = 0;
		String approvestatus = "NA";
		String status = null;
		String res039 = null;
		String res125 = null;
		String res126 = null;
		String ref = null;
		String cond = null;
		String condition = null;
		try {
			cond = isoMsg.getRes126();
			condition = cond.substring(0, 3);
			String[] responseCode = IsoPackagerGlobal.responseCode(condition);
			status = responseCode[0];
			approvestatus = responseCode[1];
			res039 = isoMsg.getRes039();
			res125 = isoMsg.getRes125();
			res126 = isoMsg.getRes126();
			ref = isoMsg.getRes037();
			Query q = entityManager.createNativeQuery(dynamicQueryForUpdate);

			q.setParameter("status", status);
			q.setParameter("str039", res039);
			q.setParameter("str125", res125);
			q.setParameter("str126", res126);
			q.setParameter("approvestatus", approvestatus);
			q.setParameter("ref", ref);
			executeUpdate = q.executeUpdate();

			logger.info("Params => status: {}, str039: {}, str125: {}, str126: {}, approveStatus: {}, ref: {}", status,
					res039, res125, res126, approvestatus, ref);
		} catch (Exception e) {
			logger.error(" interrupted while processLevelIsoStatusUpdate: {}", e.getMessage(), e);
		}

		return executeUpdate;
	}

	public boolean checkbatchExistDetail(String requestType, String type, String tablename, String count,
			String batchid) {
		boolean checkBatchExist = checkBatchExist(requestType, type, batchid);
		if (checkBatchExist) {
			return false;
		} else {

			StpProcessDetail stp = new StpProcessDetail();
			stp.setBatchcount(count);
			stp.setBatchid(batchid);
			stp.setTablename(tablename);
			stp.setRequesttype(requestType);
			stp.setType(type);
			entityManager.persist(stp);
			return true;
		}

	}

	public boolean checkBatchExist(String requestType, String type, String batchid) {
		StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM stp_process_detail ");
		sb.append("WHERE requesttype = :requestType AND type = :type AND batchid = :batchid");
		Query nativeQuery = entityManager.createNativeQuery(sb.toString());
		try {
			nativeQuery.setParameter("requestType", requestType);
			nativeQuery.setParameter("type", type);
			nativeQuery.setParameter("batchid", batchid);
			Number result = (Number) nativeQuery.getSingleResult();
			// Returns true if count > 0, false otherwise
			return result != null && result.intValue() > 0;
		} catch (Exception e) {
			throw new CustomDatabaseException(ERRORRESPONSE, e);
		}
	}

}
