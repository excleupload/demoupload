package com.example.tapp.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.entities.Report;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface ReportDao {

	Report save(Report report);

	Report update(Report report);

	Report getById(UUID id) throws RecordNotFoundException;

	void delete(Report report);

	Object[] getListByReporter(UUID userId, Page page, Sort... sorts);

	List<Report> getUserSupportList();

	Object[] getListByReporter(Sort... sorts);

	List<Report> getReportDataOfUser(UUID userId);

	Object[] getListByReplyStatus();

	List<Object[]> getUserSupportCount();

	Object[] getListByAdminnotificatonReply(UUID userId);

	Object[] getReportedUserList(HashMap<String, Object> filters);

}
