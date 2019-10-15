package com.example.tapp.data.dao;

import java.util.List;

import com.example.tapp.data.entities.TutorialPages;

public interface TutorialPagesDao {

	TutorialPages save(TutorialPages tutorialPages);

	TutorialPages getTutorialByUserId(Long id);

	TutorialPages update(TutorialPages tutorialPages);

	List<TutorialPages> getTutorialList();

	void deletePage(Long id);

}
