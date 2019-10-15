package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.TutorialPagesDao;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.entities.TutorialPages;
import com.example.tapp.data.repository.TutorialPagesRepository;

@Repository
@Transactional
public class TutorialPageDaoImpl implements TutorialPagesDao {

    @Autowired
    private TutorialPagesRepository tutorialPagesRepo;

    @Override
    public TutorialPages save(TutorialPages tutorialPages) {
        tutorialPages.setCreatedOn(new Date());
        tutorialPages.setModifiedOn(new Date());
        tutorialPages.setDeleted(false);
        return tutorialPagesRepo.save(tutorialPages);
    }

    @Override
    public TutorialPages getTutorialByUserId(Long id) {
        return tutorialPagesRepo.findById(id).orElse(null);

    }

    @Override
    public TutorialPages update(TutorialPages tutorialPages) {
        tutorialPages.setModifiedOn(new Date());
        return tutorialPagesRepo.save(tutorialPages);
    }

    @Override
    public List<TutorialPages> getTutorialList() {
        return tutorialPagesRepo.findAll(org.springframework.data.domain.Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }

    @Override
    public void deletePage(Long id) {
        tutorialPagesRepo.deleteById(id);
    }
}
