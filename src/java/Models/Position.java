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
public class Position extends Component {
    private int numberOfHours;
    private Player registeredPlayer;
    //ci je loginnuty hrac od ktoreho prisiel request registrovany na tuto poziciu
    private boolean isRegisteredPlayerForPosition;
    //ci je loginnuty hrac od ktoreho prisiel request registrovany na brigadu v ktorej je tato pozicia
    //je to ta ista hodnota ako pre cely 
    private boolean isRegisteredPlayerForBrigade;

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public Player getRegisteredPlayer() {
        return registeredPlayer;
    }

    public void setRegisteredPlayer(Player registeredPlayer) {
        this.registeredPlayer = registeredPlayer;
    }

    /**
     * ci je loginnuty hrac od ktoreho prisiel request registrovany na tuto poziciu
     * @param isRegisteredPlayerForPosition 
     */
    public void setIsRegisteredPlayerForPosition(boolean isRegisteredPlayerForPosition) {
        this.isRegisteredPlayerForPosition = isRegisteredPlayerForPosition;
    }

    public boolean isIsRegisteredPlayerForPosition() {
        return isRegisteredPlayerForPosition;
    }
    
    /**
     * ci je loginnuty hrac od ktoreho prisiel request registrovany na brigadu v ktorej je tato pozicia
     * je to ta ista hodnota ako isRegisteredPlayer pre celu brigadu ktorej sucastou je tato pozicia 
     * @return 
     */
    public void setIsRegisteredPlayerForBrigade(boolean isRegisteredPlayerForBrigade) {
        this.isRegisteredPlayerForBrigade = isRegisteredPlayerForBrigade;
    }
    
    public boolean isIsRegisteredPlayerForBrigade() {
        return isRegisteredPlayerForBrigade;
    }
    
}
