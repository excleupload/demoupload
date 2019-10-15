package com.example.tapp.business.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.TutorialService;
import com.example.tapp.common.dto.TutorialPagesDto;
import com.example.tapp.data.dao.TutorialPagesDao;
import com.example.tapp.data.entities.TutorialPages;
import com.example.tapp.data.exception.GeneralException;

@Service("tutorialservice")
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    private TutorialPagesDao tutorialPagesDao;

    @Override
    public TutorialPagesDto save(TutorialPagesDto tutorialPagesDto) throws GeneralException {
        try {
            TutorialPages tutorialpage = new TutorialPages();
            tutorialpage = tutorialpage == null ? new TutorialPages() : tutorialpage;
            tutorialpage.setContent(tutorialPagesDto.getContent());
            tutorialpage.setImagecheck(tutorialPagesDto.getImagecheck());
            tutorialpage.setVideocheck(tutorialPagesDto.getVideocheck());
            //return tutorialPagesDao.save(tutorialpage).toDto();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException("Tutorial Page not save.");
        }
    }

    @Override
    public TutorialPagesDto update(TutorialPagesDto tutorialPagesDto) throws GeneralException {
        TutorialPages tutorialpage = tutorialPagesDao.getTutorialByUserId(tutorialPagesDto.getId());
        boolean isNew = tutorialpage.getId() == null;
        try {
            if (!isNew) {
                tutorialpage.setContent(tutorialPagesDto.getContent());
                tutorialpage.setImagecheck(tutorialPagesDto.getImagecheck());
                tutorialpage.setVideocheck(tutorialPagesDto.getVideocheck());
                //return tutorialPagesDao.update(tutorialpage).toDto();
                return null;
            } else {
                throw new GeneralException("Tutorial Page not Update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException("Tutorial Page not Update.");
        }
    }

    @Override
    public List<TutorialPagesDto> getTutorial() {
        try {
            return tutorialPagesDao.getTutorialList().stream().map(TutorialPagesDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("No Record Found");
        }
    }

    @Override
    public void deletePage(Long id) {
        tutorialPagesDao.deletePage(id);
    }
}