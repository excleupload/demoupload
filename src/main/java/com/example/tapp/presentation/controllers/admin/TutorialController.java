package com.example.tapp.presentation.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.TutorialService;
import com.example.tapp.common.dto.TutorialPagesDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import static com.example.tapp.common.response.ResponseUtils.success;
@RestController
@RequestMapping("/admin")
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @PostMapping("/tutorial/savetutorial")
    public ResponseEntity<?> saveContextType(
            @RequestBody TutorialPagesDto tutorialpagesDto)
            throws GeneralException, RecordNotFoundException {
        return success.apply(tutorialService.save(tutorialpagesDto));
        }
    @PostMapping("/tutorial/updatesavetutorial")
    public ResponseEntity<?> updateContextType(@RequestBody TutorialPagesDto tutorialpagesDto)
            throws GeneralException, RecordNotFoundException {
        return success.apply(tutorialService.update(tutorialpagesDto));
    }

    @GetMapping("/tutorial/listtutorial")
    public ResponseEntity<?> getTutorialData() {
        return success.apply(tutorialService.getTutorial());
    }

    @RequestMapping(value = "/tutorial/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteStaticType(@PathVariable("id") Long id) {
        tutorialService.deletePage(id);
        return success.apply("Static Page successfully deleted.");
    }
}