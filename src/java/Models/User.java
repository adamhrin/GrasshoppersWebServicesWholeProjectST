/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Adam
 */
import java.io.Serializable;  
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name = "user") 

public class User implements Serializable {  
   private static final long serialVersionUID = 1L; 
   private int id; 
   private String name; 
   private String profession;  
   private List<Training> trainings;
   public User(){} 
    
   public User(int id, String name, String profession){  
      this.id = id; 
      this.name = name; 
      this.profession = profession; 
      this.trainings = new ArrayList<Training>();
   }  
   public int getId() { 
      return id; 
   }  
   @XmlElement 
   public void setId(int id) { 
      this.id = id; 
   } 
   public String getName() { 
      return name; 
   } 
   @XmlElement
   public void setName(String name) { 
      this.name = name; 
   } 
   public String getProfession() { 
      return profession; 
   } 
   @XmlElement 
   public void setProfession(String profession) { 
      this.profession = profession; 
   }   
   public void addTraining(Training training) { 
      this.trainings.add(training);
   } 
   public List<Training> getTrainings()
   {
       return trainings;
   }    
} 
