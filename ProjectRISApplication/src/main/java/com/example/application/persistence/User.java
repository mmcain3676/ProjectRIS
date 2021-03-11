package com.example.application.persistence;

import java.util.*;
 
import javax.persistence.*;
 
@Entity
@Table(name = "users")
public class User {
 
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
 
    private String username;
    private String password;
    private boolean enabled;
    private String full_name;
     
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
    private Set<Role> roles = new HashSet<>();
 
    public Long getId() {
        return user_id;
    }

    public String getUsername(){
      return this.username;
    }

    public String getPassword(){
      return this.password;
    }

    public boolean isEnabled(){
      return this.enabled;
    }

    public void setUsername(String username){
      this.username = username;
    }

    public void setPassword(String password){
      this.password = password;
    }

    public void setEnabled(boolean enabled){
      this.enabled = enabled;
    }

    public Set<Role> getRoles() {
      return this.roles;
    }

    public String getRoleNames() {
      String roleNames = "";
      Set<Role> roleSet = this.getRoles();
      for (Role name : roleSet)
      {
        if(roleNames.length() > 0)
          roleNames += ("\n" + name.getName());
        else
          roleNames += name.getName(); 
      }
      return roleNames;
    }

    public String getFullName() {
      return this.full_name;
    }
 
    // remaining getters and setters are not shown for brevity
}



