package com.example.tapp.presentation.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.response.ResponseUtils.error;
import com.example.tapp.business.service.ShareContentService;
import com.example.tapp.common.dto.ShareContentDto;

@RestController
@RequestMapping("/admin/share-content")
public class ShareContentController {

    @Autowired
    public ShareContentService contentService;

    @RequestMapping("/save")
    public ResponseEntity<?> save(@RequestBody ShareContentDto dto) {
        //
        contentService.saveContent(dto);
        return success.apply("Share content added.");
    }

    @RequestMapping("/update")
    public ResponseEntity<?> update(@RequestBody ShareContentDto dto) {
        //
        if (null != contentService.updateContent(dto)) {
            return success.apply("Share content updated.");
        }
        return error.apply("Share content not updated.");
    }

    @RequestMapping("/list")
    public ResponseEntity<?> list() {
        //
        return success.apply(contentService.getShareContent());
    }

}