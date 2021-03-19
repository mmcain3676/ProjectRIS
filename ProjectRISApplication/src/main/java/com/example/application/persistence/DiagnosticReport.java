package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "diagnosticreport")
public class DiagnosticReport {
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long report;
    private Long patient;
    private Long radiologist;
    private Long appointment;
    @Column(name = "imaging_info")
    private Long imaging;
    private String notes;

    public void setReport(Long report) {
        this.report = report;
    }

    public Long getReport() {
        return this.report;
    }
    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getPatient() {
        return this.patient;
    }

    public void setRadiologist(Long radiologist) {
        this.radiologist = radiologist;
    }

    public Long getRadiologist() {
        return this.radiologist;
    }

    public void setAppointment(Long appointment) {
        this.appointment = appointment;
    }

    public Long getAppointment() {
        return this.appointment;
    }

    public void setImaging(Long imaging) {
        this.imaging = imaging;
    }

    public Long getImaging() {
        return this.imaging;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }

}