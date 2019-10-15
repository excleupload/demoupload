package com.example.tapp.presentation.controllers;

import static com.example.tapp.common.response.ResponseUtils.errorList;
import static com.example.tapp.common.response.ResponseUtils.sendImage;
import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.utils.Route.reportDetails;
import static com.example.tapp.common.utils.Route.reportFile;
import static com.example.tapp.common.utils.Route.reportImage;
import static com.example.tapp.common.utils.Route.reportRemove;
import static com.example.tapp.common.utils.Route.reportUpdate;
import static com.example.tapp.common.utils.Route.reportUser;
import static com.example.tapp.common.utils.Route.reportUserList;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tapp.business.service.ReportService;
import com.example.tapp.common.dto.ReportDto;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.data.entities.Report;
import com.example.tapp.data.exception.GeneralException;
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(reportFile)
    public ResponseEntity<?> reportFile(@RequestParam(name = "file", required = false) MultipartFile file)
            throws GeneralException {
        return success.apply(reportService.saveReportFile(file));
    }

    @PostMapping(reportUser)
    public ResponseEntity<?> reportUser(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("reporterId") UUID reporterId, @RequestParam("reportedId") UUID reportedId,
            @RequestParam("reason") String reason) throws GeneralException {
        //
        ReportDto dto = new ReportDto();
        dto.setReporterId(reporterId);
        dto.setReportedId(reportedId);
        dto.setReason(reason);
        dto.setIsRead(false);
        return success.apply(reportService.saveReport(file, dto));
    }

    @GetMapping(reportDetails)
    public ResponseEntity<?> details(@RequestParam("reportId") UUID reportId) throws GeneralException {
        return success.apply(reportService.getReportById(reportId));
    }

    @PostMapping(reportRemove)
    public ResponseEntity<?> removeReport(@RequestParam("reportId") UUID reportId) throws GeneralException {
        reportService.removeReport(reportId);
        return success.apply("Report removed.");
    }

    @PostMapping(reportUpdate)
    public ResponseEntity<?> updateReport(@RequestBody ReportDto dto, BindingResult result) throws GeneralException {
        new Report(ValidationWhen.UPDATE).validate(dto, result);
        if (result.hasErrors())
            return errorList.apply(result.getFieldErrors());

        return success.apply(reportService.update(dto));
    }

    @GetMapping(reportImage)
    public void reportImage(@PathVariable("reportId") UUID reportId, HttpServletResponse response) throws IOException {
        sendImage(reportService.getReportFile(reportId), response);
    }

    @GetMapping(reportUserList)
    public ResponseEntity<?> reportUserList(@RequestParam("userId") UUID userId, HttpServletRequest request) {
        return success.apply(reportService.getReportListByReporter(userId, setOptions(request)));
    }

    public HashMap<String, Object> setOptions(HttpServletRequest request) {
        HashMap<String, Object> options = new HashMap<>();
        // Sort.setSortOptions(options, request);
        Page.setPageOptions(options, request);
        return options;
    }
}