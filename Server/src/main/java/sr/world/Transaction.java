
//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.


package sr.world;

import java.io.Serializable;

public class Transaction implements Serializable{
  static final long serialVersionUID = 1L;
  Player recipient;
  int sum;

  public Transaction(Player recipient, int sum){
    this.recipient = recipient;
    this.sum = sum;
  }

  public Transaction(Transaction oldTransaction){
    this.recipient = oldTransaction.getRecipient();
    this.sum = oldTransaction.getSum();
  }

  public void performTransaction(){
  }

  public int getSum(){
    return sum;
  }

  public Player getRecipient(){
    return recipient;
  }
}