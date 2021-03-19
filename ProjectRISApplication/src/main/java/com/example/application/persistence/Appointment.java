package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patient;
    private Long modality;
    @Column(name = "date_time")
    private String datetime;
    private Long radiologist;
    private Long technician;

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getPatient() {
        return this.patient;
    }

    public void setModality(Long modality) {
        this.modality = modality;
    }

    public Long getModality() {
        return this.modality;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setRadiologist(Long radiologist) {
        this.radiologist = radiologist;
    }

    public Long getRadiologist() {
        return this.radiologist;
    }

    public void setTechnician(Long technician) {
        this.technician = technician;
    }

    public Long getTechnicain() {
        return this.technician;
    }

}