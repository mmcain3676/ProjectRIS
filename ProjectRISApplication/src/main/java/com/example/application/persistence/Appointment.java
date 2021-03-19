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
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "email_address")
    private String emailaddress;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

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

    public Long getTechnician() {
        return this.technician;
    }

    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber(){
        return this.phonenumber;
    }

    public void setEmailaddress(String emailaddress){
        this.emailaddress = emailaddress;
    }

    public String getEmailaddress(){
        return this.emailaddress;
    }

}