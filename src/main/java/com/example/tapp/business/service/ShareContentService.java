package com.example.tapp.business.service;

import java.util.List;

import com.example.tapp.common.dto.ShareContentDto;

public interface ShareContentService {

	ShareContentDto saveContent(ShareContentDto dto);

	ShareContentDto updateContent(ShareContentDto dto);

	List<ShareContentDto> getShareContent();

}
