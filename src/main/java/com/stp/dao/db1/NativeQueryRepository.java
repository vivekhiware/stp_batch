package com.stp.dao.db1;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_TTUMQuery;
import com.stp.utility.TTumRequest;

@Repository
@Transactional
public class NativeQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	// Execute a native query and return results
	public List<Object[]> executeNativeQuery(STP_TTUMQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQuery());
		query.append(" LIMIT ");
		query.append(req.getPagination());
		query.append(" OFFSET 0");
		System.err.println("query" + query.toString());
		Integer paramcount = ttum.getParamcount();
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			if (paramcount >= 1) {
//				nativeQuery.setParameter("a", req.getReqdate());
			}
			if (paramcount >= 2) {
//				nativeQuery.setParameter("b", req.getCycle());
			}
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());

		} catch (IllegalArgumentException e) {
			System.err.println("Error setting query parameters: " + e.getMessage());
			throw e; // Rethrow the exception or handle it as needed
		}
		try {
			return nativeQuery.getResultList();
		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			throw e; // Rethrow or handle as needed
		}
	}

	public List<Object[]> executeNativeQueryCount(STP_TTUMQuery ttum, TTumRequest req) {
		StringBuilder query = new StringBuilder(ttum.getQueryforCount());
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			throw e; // Rethrow or handle as needed
		}
	}

	public List<Object[]> getSTP_REPORTQUERY(String queryDetail, TTumRequest req) {
		StringBuilder query = new StringBuilder(queryDetail);
		Query nativeQuery = entityManager.createNativeQuery(query.toString());
		try {
			nativeQuery.setParameter("a", req.getFromdate());
			nativeQuery.setParameter("b", req.getTodate());
			return nativeQuery.getResultList();
		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			throw e;
		}
	}
}
