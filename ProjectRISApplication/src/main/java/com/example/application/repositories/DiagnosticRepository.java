package com.example.application.repositories;

import com.example.application.persistence.DiagnosticReport;

import org.springframework.data.repository.CrudRepository;
 
public interface DiagnosticRepository extends CrudRepository<DiagnosticReport, Long> {
 
}