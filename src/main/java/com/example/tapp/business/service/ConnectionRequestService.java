package com.example.tapp.business.service;

import java.util.HashMap;
import java.util.UUID;

import com.example.tapp.common.dto.ConnectionRequestDto;
import com.example.tapp.common.dto.RequestOperation;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.GeneralException;

public interface ConnectionRequestService {

	ConnectionRequestDto add(ConnectionRequestDto dto) throws GeneralException;

	void operation(RequestOperation operation) throws GeneralException;

	PageResponse<?> getList(UUID userId, HashMap<String, Object> options);

	void createRequestForSystemUser(UUID userId);

}
