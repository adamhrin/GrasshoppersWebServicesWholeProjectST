/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

/**
 *
 * @author Adam
 */
import CustomException.CustomException;
import Dao.UserDao;
import Models.User;
import java.util.List; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;  

@Path("secured/UserService")
public class UserService {  
   UserDao userDao = new UserDao();  
   @GET 
   @Path("/users") 
   @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
   public List<User> getUsers() throws CustomException{ 
        return userDao.getAllUsers(); 
    }  
}
