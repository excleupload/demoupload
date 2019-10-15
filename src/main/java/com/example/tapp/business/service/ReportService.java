package com.example.tapp.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.tapp.common.dto.ReportDto;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.GeneralException;

public interface ReportService {

	HashMap<String, String> saveReportFile(MultipartFile file) throws GeneralException;

	ReportDto saveReport(MultipartFile file, ReportDto dto) throws GeneralException;

	void removeReport(UUID reportId) throws GeneralException;

	ReportDto getReportById(UUID reportId) throws GeneralException;

	ReportDto update(ReportDto dto) throws GeneralException;

	Object[] getReportFile(UUID reportId);

	PageResponse<?> getReportListByReporter(UUID reporterId, HashMap<String, Object> options);

	PageResponse<?> getReportListByReporter(HashMap<String, Object> s);

	ReportDto adminReportUpdate(ReportDto dto, UUID reportId) throws GeneralException;

	List<Object[]> getUserSupportCount();

	PageResponse<?> getReportListByNotification(UUID reporterId);

	PageResponse<?> getUserReply();

}
