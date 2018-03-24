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
import Dao.BrigadesDao;
import Dao.CategoriesDao;
import Dao.ComponentsDao;
import Dao.GoalsDao;
import Dao.InfoDao;
import Dao.TrainingsDao;
import Dao.LeaguesDao;
import Dao.LocationsDao;
import Dao.MatchesDao;
import Dao.PlayersDao;
import Dao.PositionsDao;
import Models.Brigade;
import Models.Category;
import Models.Goal;
import Models.Info;
import Models.League;
import Models.Location;
import Models.Match;
import Models.Player;
import Models.Position;
import Models.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET; 
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path; 
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;  
import javax.ws.rs.core.Response;

@Path("secured/GrasshoppersService")
public class GrasshoppersService {
    TrainingsDao trainingsDao = new TrainingsDao();  
    BrigadesDao brigadesDao = new BrigadesDao();
    CategoriesDao categoriesDao = new CategoriesDao();
    LocationsDao locationsDao = new LocationsDao();
    LeaguesDao leaguesDao = new LeaguesDao();
    PositionsDao positionsDao = new PositionsDao();
    MatchesDao matchesDao = new MatchesDao();
    InfoDao infoDao = new InfoDao();
    PlayersDao playersDao = new PlayersDao();
    GoalsDao goalsDao = new GoalsDao();
    ComponentsDao componentsDao = new ComponentsDao();
    ObjectMapper mapper = new ObjectMapper();
    
    /**
     * GET
     */
    
    @GET
    @Path("player/{id_player}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("id_player") int idPlayer) {
        Response response = null;
        try {
            response = Response.ok().entity(playersDao.getPlayer(idPlayer)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/player/{id_player}/trainings") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainingsForPlayer(@PathParam("id_player") int idPlayer){ 
        Response response = null;
        try {
            response = Response.ok().entity(trainingsDao.getTrainingsForPlayer(idPlayer)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/player/{id_player}/brigades") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBrigadesForPlayer(@PathParam("id_player") int idPlayer){ 
        Response response = null;
        try {
            response = Response.ok().entity(brigadesDao.getBrigadesForPlayer(idPlayer)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    } 
    
    @GET 
    @Path("/player/{id_player}/matches") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchesForPlayer(@PathParam("id_player") int idPlayer){ 
        Response response = null;
        try {
            response = Response.ok().entity(matchesDao.getMatchesForPlayer(idPlayer)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/player/{id_player}/match/{id_match}") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchById(@PathParam("id_player") int idPlayer, @PathParam("id_match") int idMatch){ 
        Response response = null;
        try {
            response = Response.ok().entity(matchesDao.getMatchById(idPlayer, idMatch)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/player/{id_player}/info") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoForPlayer(@PathParam("id_player") int idPlayer){ 
        Response response = null;
        try {
            response = Response.ok().entity(infoDao.getInfoForPlayer(idPlayer)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/categories") 
    @Produces(MediaType.APPLICATION_JSON)
    public /*List<Category>*/ Response getCategories(){ 
        Response response = null;
        try {
            response = Response.ok().entity(categoriesDao.getCategories()).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/category/{id_category}/players") 
    @Produces(MediaType.APPLICATION_JSON)
    public /*List<Category>*/ Response getPlayersForCategory(@PathParam("id_category") int idCategory){ 
        Response response = null;
        try {
            response = Response.ok().entity(playersDao.getPlayersForCategory(idCategory)).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/locations") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocations(){ 
        Response response = null;
        try {
            response = Response.ok().entity(locationsDao.getLocations()).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/leagues") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeagues(){ 
        Response response = null;
        try {
            response = Response.ok().entity(leaguesDao.getLeagues()).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    @GET 
    @Path("/positions") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPositions(){ 
        Response response = null;
        try {
            response = Response.ok().entity(positionsDao.getPositions()).build();
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
//    
//    @GET 
//    @Path("/brigades") 
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Brigade> getBrigadesForPlayer(){ 
//        return brigadesDao.getBrigadesForPlayer();
//    }
//    
//    @GET 
//    @Path("/brigades") 
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Brigade> getBrigadesForPlayer(){ 
//        return brigadesDao.getBrigadesForPlayer();
//    }
    
    
    
    
    
    
    
    
    /**
     * POST
     */
    
    @POST
    @Path("/training") 
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertTraining(String json) { 
        try {
            Training t = (Training)mapObject(json, Training.class);
            trainingsDao.insertTraining(t);
        } catch (Exception ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        } 
//        catch (CustomException ex) {
//            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
//        }
        return Response.ok().build();
    }
    
    @POST
    @Path("/match") 
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertMatch(String json) { 
        try {
            Match m = (Match)mapObject(json, Match.class);
            matchesDao.insertMatch(m);
        } catch (Exception ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        } 
//        catch (CustomException ex) {
//            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
//        }
        return Response.ok().build();
    }
    
    @POST
    @Path("/brigade") 
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertBrigade(String json) { 
        try {
            Brigade b = (Brigade)mapObject(json, Brigade.class);
            brigadesDao.insertBrigade(b);
        } catch (Exception ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        } 
//        catch (CustomException ex) {
//            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
//        }
        return Response.ok().build();
    }
    
    @POST
    @Path("/info") 
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertInfo(String json) { 
        try {
            Info i = (Info)mapObject(json, Info.class);
            infoDao.insertInfo(i);
        } catch (Exception ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        } 
//        catch (CustomException ex) {
//            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
//        }
        return Response.ok().build();
    }
    
    @POST
    @Path("category")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertCategory(String json) {
        Category category = null;
        try {
            category = (Category)mapObject(json, Category.class);
            categoriesDao.insertComponent(category, "category");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("league")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertLeague(String json) {
        League league = null;
        try {
            league = (League)mapObject(json, League.class);
            leaguesDao.insertComponent(league, "league");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("location")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertLocation(String json) {
        Location location = null;
        try {
            location = (Location)mapObject(json, Location.class);
            locationsDao.insertComponent(location, "location");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("position")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertPosition(String json) {
        Position position = null;
        try {
            position = (Position)mapObject(json, Position.class);
            positionsDao.insertComponent(position, "position");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("match/{id_match}/grassGoal")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertGrassGoal(String json, @PathParam("id_match") int idMatch) {
        Goal goal = null;
        try {
            goal = (Goal)mapObject(json, Goal.class);
            goalsDao.insertGrassGoal(goal, idMatch);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("match/{id_match}/opponentGoal")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertOpponentGoal(String json, @PathParam("id_match") int idMatch) {
        Goal goal = null;
        try {
            goal = (Goal)mapObject(json, Goal.class);
            goalsDao.insertOpponentGoal(goal, idMatch);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    
    
    
    
    
    
    
    /**
     * PUT
     */
    
    @PUT
    @Path("player")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePlayer(String json) {
        Player player = null;
        try {
            player = (Player)mapObject(json, Player.class);
            playersDao.updatePlayer(player);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().entity(player).build();
    }
    
    @PUT
    @Path("player/{id_player}/accept/training/{id_training}")
    public Response updatePlayerAcceptsTraining(@PathParam("id_player") int idPlayer, @PathParam("id_training") int idTraining) {
        try {
            trainingsDao.updatePlayerOnTraining(idPlayer, idTraining, "b'1'");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("player/{id_player}/decline/training/{id_training}")
    public Response updatePlayerDeclinesTraining(@PathParam("id_player") int idPlayer, @PathParam("id_training") int idTraining) {
        try {
            trainingsDao.updatePlayerOnTraining(idPlayer, idTraining, "b'0'");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("brigade")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBrigade(String json) {
        Brigade brigade = null;
        try {
            brigade = (Brigade)mapObject(json, Brigade.class);
            brigadesDao.updateBrigade(brigade);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    
    @PUT
    @Path("match")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMatch(String json) {
        Match match = null;
        try {
            match = (Match)mapObject(json, Match.class);
            matchesDao.updateMatch(match);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("info")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateInfo(String json) {
        Info info = null;
        try {
            info = (Info)mapObject(json, Info.class);
            infoDao.updateInfo(info);
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("category")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCategory(String json) {
        Category category = null;
        try {
            category = (Category)mapObject(json, Category.class);
            categoriesDao.updateComponent(category, "category");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("league")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLeague(String json) {
        League league = null;
        try {
            league = (League)mapObject(json, League.class);
            leaguesDao.updateComponent(league, "league");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("location")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLocation(String json) {
        Location location = null;
        try {
            location = (Location)mapObject(json, Location.class);
            locationsDao.updateComponent(location, "location");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("position")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePosition(String json) {
        Position position = null;
        try {
            position = (Position)mapObject(json, Position.class);
            positionsDao.updateComponent(position, "position");
        } catch (IOException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_UPDATE_INSERT) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("player/{id_player}/brigade/{id_brigade}/position/{id_position}/registration")
    public Response registerPlayerOnPosition(@PathParam("id_player") int idPlayer, @PathParam("id_brigade") int idBrigade, @PathParam("id_position") int idPosition) {
        try {
            positionsDao.registerPlayerOnPosition(idPlayer, idBrigade, idPosition);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path("player/{id_player}/brigade/{id_brigade}/position/{id_position}/unregistration")
    public Response unregisterPlayerOnPosition(@PathParam("id_player") int idPlayer, @PathParam("id_brigade") int idBrigade, @PathParam("id_position") int idPosition) {
        try {
            positionsDao.unregisterPlayerOnPosition(idPlayer, idBrigade, idPosition);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
    
    
    
    
    
    
    
    /**
     * DELETE
     */
    
    @DELETE
    @Path("training/{id_training}")
    public Response deleteTraining(@PathParam("id_training") int idTraining) {
        try {
            componentsDao.deleteComponent(idTraining, "training");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("brigade/{id_brigade}")
    public Response deleteBrigade(@PathParam("id_brigade") int idBrigade) {
        try {
            componentsDao.deleteComponent(idBrigade, "brigade");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("match/{id_match}")
    public Response deleteMatch(@PathParam("id_match") int idMatch) {
        try {
            componentsDao.deleteComponent(idMatch, "match");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("info/{id_info}")
    public Response deleteInfo(@PathParam("id_info") int idInfo) {
        try {
            componentsDao.deleteComponent(idInfo, "info");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("category/{id_category}")
    public Response deleteCategory(@PathParam("id_category") int idCategory) {
        try {
            categoriesDao.deleteComponent(idCategory, "category");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("league/{id_league}")
    public Response deleteLeague(@PathParam("id_league") int idLeague) {
        try {
            leaguesDao.deleteComponent(idLeague, "league");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("location/{id_location}")
    public Response deleteLocation(@PathParam("id_location") int idLocation) {
        try {
            locationsDao.deleteComponent(idLocation, "location");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("position/{id_position}")
    public Response deletePosition(@PathParam("id_position") int idPosition) {
        try {
            positionsDao.deleteComponent(idPosition, "position");
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("grassGoal/{id_goal}")
    public Response deleteGrassGoal(@PathParam("id_goal") int idGoal) {
        try {
            goalsDao.deleteGrassGoal(idGoal);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    @DELETE
    @Path("opponentGoal/{id_goal}")
    public Response deleteOpponentGoal(@PathParam("id_goal") int idGoal) {
        try {
            goalsDao.deleteOpponentGoal(idGoal);
        } catch (CustomException ex) {
            Logger.getLogger(GrasshoppersService.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            if (ex.getErrorCode() == CustomException.ERR_DELETE) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            }
        }
        return Response.ok().build();
    }
    
    private Object mapObject(String json, Class c) throws IOException {
        return mapper.readValue(json, c);
    }
}
