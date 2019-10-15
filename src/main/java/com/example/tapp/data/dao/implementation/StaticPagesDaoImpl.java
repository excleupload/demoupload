package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.data.dao.StaticPagesDao;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.StaticPagesRepository;

@Repository
@Transactional
public class StaticPagesDaoImpl implements StaticPagesDao {

    @Autowired
    private StaticPagesRepository staticPagesRepo;
    @Override
    public StaticPages save(StaticPages staticPages) {
        staticPages.setCreatedOn(new Date());
        staticPages.setModifiedOn(new Date());
        staticPages.setDeleted(false);
        return staticPagesRepo.save(staticPages);
    }

    @Override
    public StaticPages saveorExitCheck(StaticPages staticPages) {
        staticPages.setCreatedOn(new Date());
        staticPages.setModifiedOn(new Date());
        staticPages.setDeleted(false);
        return staticPagesRepo.save(staticPages);
    }

    @Override
    public StaticPages update(StaticPages staticPages) {
        staticPages.setModifiedOn(new Date());
        return staticPagesRepo.save(staticPages);
    }

    @Override
    public StaticPages getDetailsById(Long id) throws RecordNotFoundException {
        return staticPagesRepo.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public StaticPages getByPageType(PageType pageType) {

        return staticPagesRepo.findByType(pageType).orElse(new StaticPages());
    }
   
    @Override
    public StaticPages getByPageTypeForApi(PageType pageType) throws RecordNotFoundException {

        return staticPagesRepo.findByType(pageType).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public void deletePage(Long id) {
        staticPagesRepo.deleteById(id);
    }
    @Override
    public List<StaticPages> getList() {
        return staticPagesRepo.findAll(org.springframework.data.domain.Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }
   
}


