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
import Dao.PlayersDao;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * @author Adam
 * SecurityFilter for BASIC AUTH
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    
    //TOTO BUDE INAK, nech je resource pre vsetky zabezpecene requesty jednotne, napr. /secured
    private static final String SECURED_URL_PREFIX = "secured"; //vsetky url ktore obsahuju resource /secured su authorizovane
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0) { //request obsahuje header
                String authToken = authHeader.get(0); //prvy header (mal by byt vzdy len jeden)
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, ""); //odstranime Basic_ z headeru
                String decodedString = Base64.decodeAsString(authToken); //username:password
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken(); //username
                String password = tokenizer.nextToken(); //password
                
                PlayersDao playersDao = new PlayersDao();
                try {
                    if (playersDao.existsPlayer(username, password)) {
                        return; //povol pristup na resource
                    }
                } catch (CustomException ex) {
                    Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
                    Response customExceptionCaught = Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(ex)
                        .build();
                    requestContext.abortWith(customExceptionCaught);
                }
            }
            Response unauthorizedStatus = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("You cannot access this resource")
                    .build();
            requestContext.abortWith(unauthorizedStatus);
        }
    }
}

