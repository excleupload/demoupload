package com.example.tapp.business.service;

import java.util.List;

import com.example.tapp.common.dto.StaticPagesDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface StaticPagesService {

	StaticPagesDto save(StaticPagesDto staticPagesDto) throws GeneralException;

	StaticPagesDto getDetailById(Long id) throws RecordNotFoundException;

	StaticPagesDto saveorExitCheck(StaticPagesDto staticPagesDto) throws GeneralException;

	void deletePage(Long id);

	List<StaticPagesDto> getList();

	StaticPagesDto getPageByType(String type);

	StaticPagesDto getPageTypeForApi(String type) throws RecordNotFoundException;

}
