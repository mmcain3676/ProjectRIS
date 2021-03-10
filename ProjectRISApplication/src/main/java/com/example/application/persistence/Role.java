package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
     
    private String name;

    public Integer getId() {
        return id;
    }
     
    public String getName(){
        return this.name;
    }  

    public void setName(String name){
        this.name = name;
    }
}
