package com.example.tapp.business.service;

import java.util.List;

import com.example.tapp.common.dto.TutorialPagesDto;
import com.example.tapp.data.exception.GeneralException;

public interface TutorialService {

	TutorialPagesDto save(TutorialPagesDto tutorialPagesDto) throws GeneralException;

	void deletePage(Long id);

	List<TutorialPagesDto> getTutorial();

	TutorialPagesDto update(TutorialPagesDto tutorialPagesDto) throws GeneralException;

}
