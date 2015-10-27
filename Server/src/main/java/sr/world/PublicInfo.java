package sr.world;

import java.io.Serializable;

public class PublicInfo implements Serializable{
    static final long serialVersionUID = 1L;
    int turn;

    public PublicInfo(){
        turn = 0;
    }

    public int getTurn(){
        return turn;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }
}