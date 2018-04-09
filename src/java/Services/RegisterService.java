/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import CustomException.CustomException;
import Dao.PlayersDao;
import Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Adam
 */
@Path("GrasshoppersService")
public class RegisterService {
    PlayersDao playersDao = new PlayersDao();
    ObjectMapper mapper = new ObjectMapper();
    
    @POST
    @Path("player/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerPlayer(String json) {
        Player player = null;
        try {
            player = (Player)this.mapObject(json, Player.class);
            try {
                playersDao.registerPlayer(player);
            } catch (CustomException ex) {
                Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
                if (ex.getErrorCode() == CustomException.ERR_CONFLICT) {
                    return Response.status(Response.Status.CONFLICT).entity(ex).build();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("player/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPlayer(String json) {
        Player player = null;
        Player playerToReturn = null;
        try {
            player = (Player)this.mapObject(json, Player.class);
            try {
                playerToReturn = playersDao.loginPlayer(player);
            } catch (CustomException ex) {
                Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
                if (ex.getErrorCode() == CustomException.ERR_BAD_LOGIN_DATA) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).entity(ex).build();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(playerToReturn).build();
    }
    
    private Object mapObject(String json, Class c) throws IOException {
        return mapper.readValue(json, c);
    }
}
