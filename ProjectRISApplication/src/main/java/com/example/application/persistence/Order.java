package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patient;
    private Long modality;
    private Long referral_md;
    private String notes;
    private Long status;
    private Long report;
    


    //      GETTERS

    public Long getId(){
        return this.id;
    }

    public Long getPatient(){
        return this.patient;
    }

    public Long getModality(){
        return this.modality;
    }

    public Long getReferral_md(){
        return this.referral_md;
    }

    public String getNotes(){
        return this.notes;
    }

    public Long getStatus(){
        return this.status;
    }

    public Long getReport(){
        return this.report;
    }


    //      SETTERS

    public void setId(Long id){
        this.id = id;
    }

    public void setPatient(Long patient){
        this.patient = patient;
    }

    public void setModality(Long modality){
        this.modality = modality;
    }

    public void setReferral_md(Long referral_md){
        this.referral_md = referral_md;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public void setStatus(Long status){
        this.status = status;
    }

    public void setReport(Long report){
        this.report = report;
    }

    



}