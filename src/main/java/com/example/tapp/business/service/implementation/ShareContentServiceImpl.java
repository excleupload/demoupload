package com.example.tapp.business.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.ShareContentService;
import com.example.tapp.common.dto.ShareContentDto;
import com.example.tapp.data.dao.ShareContentDao;
import com.example.tapp.data.entities.ShareContent;

@Service
public class ShareContentServiceImpl implements ShareContentService {

    @Autowired
    private ShareContentDao contentDao;

    @Override
    public ShareContentDto saveContent(ShareContentDto dto) {
        ShareContent content = new ShareContent();
        content.setTextContent(dto.getTextContent());
        content.setAppLink(dto.getAppLink());
        return contentDao.save(content).toDto();
    }

    @Override
    public ShareContentDto updateContent(ShareContentDto dto) {
        ShareContent persist = contentDao.getById(dto.getId());
        if (persist == null)
            return null;

        persist.setTextContent(dto.getTextContent());
        persist.setAppLink(dto.getAppLink());
        return contentDao.update(persist).toDto();
    }

    @Override
    public List<ShareContentDto> getShareContent() {
        return contentDao.getList().stream().map(ShareContent::toDto).collect(Collectors.toList());
    }

}
