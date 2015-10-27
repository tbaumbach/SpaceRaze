package sr.world;

import java.io.Serializable;

public class UniqueIdCounter implements Serializable{
  static final long serialVersionUID = 1L;
  private int uniqueIdCounter = 0;

  public int getUniqueId(){
    uniqueIdCounter++;
    return uniqueIdCounter;
  }
}
