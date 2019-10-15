package com.example.tapp.presentation.controllers.admin;
import static com.example.tapp.common.response.ResponseUtils.sendImage;


import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.ReportService;
import com.example.tapp.common.dto.ReportDto;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.data.exception.GeneralException;
import static com.example.tapp.common.response.ResponseUtils.success;

@RestController
@RequestMapping("/reportService")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/userSupportList")
    public ResponseEntity<?> getUserSupportListData(HttpServletRequest request) {

        return success.apply(reportService.getReportListByReporter(setOptions(request)));
    }

    @GetMapping("/imageshow/{reportId}")
    public void reportImage(@PathVariable("reportId") UUID reportId, HttpServletResponse response) throws IOException {
        sendImage(reportService.getReportFile(reportId), response);
    }

    public HashMap<String, Object> setOptions(HttpServletRequest request) {
        HashMap<String, Object> options = new HashMap<>();
        Page.setPageOptions(options, request);

        return options;
    }

    @PostMapping("/replyreport/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable("reportId") UUID reportId, @RequestBody ReportDto dto)
            throws GeneralException {
        return success.apply(reportService.adminReportUpdate(dto, reportId));
    }


}