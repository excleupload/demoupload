package com.example.tapp.data.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.Report;

public interface ReportRepository  extends JpaRepository<Report,UUID>{

	List<Report> findByReporterUserIdOrReportedUserId(UUID userId, UUID userId2);

}
