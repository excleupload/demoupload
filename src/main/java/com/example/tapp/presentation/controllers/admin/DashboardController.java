package com.example.tapp.presentation.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.DashboardService;
import com.example.tapp.common.response.ResponseUtils;

@RestController
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/counts")
    public ResponseEntity<?> getCounts() {
        //
        return ResponseUtils.success.apply(dashboardService.counts());
    }

    @GetMapping("/dashboard/bar-chart")
    public ResponseEntity<?> getBarChartdata(@RequestParam("year") Integer year) {
        //
        return ResponseUtils.success.apply(dashboardService.getUserData(year));
    }
}
