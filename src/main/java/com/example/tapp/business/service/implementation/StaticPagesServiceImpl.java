package com.example.tapp.business.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.StaticPagesService;
import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.common.dto.StaticPagesDto;
import com.example.tapp.data.dao.StaticPagesDao;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;

@Service("staticpageservice")
public class StaticPagesServiceImpl implements StaticPagesService {

    @Autowired
    private StaticPagesDao staticPagesDao;

    @Override
    public StaticPagesDto save(StaticPagesDto staticPagesDto) throws GeneralException {
        StaticPages staticPages = staticPagesDao.getByPageType(staticPagesDto.getType());
        boolean isNew = staticPages.getId() == null;
        try {
            if (!isNew) {
                staticPages.setType(staticPagesDto.getType());
                staticPages.setContent(staticPagesDto.getContent());
                return staticPagesDao.update(staticPages).toDto();
            } else {
                throw new GeneralException("Static page Type MisMatch.");
            }
        } catch (Exception e) {
            throw new GeneralException("Static page Type MisMatch.");
        }
    }

    @Override
    public StaticPagesDto getDetailById(Long id) throws RecordNotFoundException {
        return staticPagesDao.getDetailsById(id).toDto();
    }

    @Override
    public StaticPagesDto saveorExitCheck(StaticPagesDto staticPagesDto) throws GeneralException {
        StaticPages staticPages = staticPagesDao.getByPageType(staticPagesDto.getType());
        boolean isNew = staticPages.getId() == null;
        try {
            if (isNew) {
                staticPages.setType(staticPagesDto.getType());
                staticPages.setContent(staticPagesDto.getContent());
                return staticPagesDao.saveorExitCheck(staticPages).toDto();
            } else {
                throw new GeneralException("You have already Added This Type.");
            }
        } catch (Exception e) {
            throw new GeneralException("You have already Added This Type.");
        }
    }

    @Override
    public void deletePage(Long id) {
        staticPagesDao.deletePage(id);
    }

    @Override
    public List<StaticPagesDto> getList() {
        try {
            return staticPagesDao.getList().stream().map(StaticPagesDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Static Type MisMatch");
        }

    }

    @Override
    public StaticPagesDto getPageByType(String type) {
        PageType pageType = PageType.valueOf(type);
        return staticPagesDao.getByPageType(pageType).toDto();
    }

    @Override
    public StaticPagesDto getPageTypeForApi(String type) throws RecordNotFoundException {
        PageType pageType = PageType.valueOf(type);
        return staticPagesDao.getByPageTypeForApi(pageType).toDto();
    }

}
