package sr.client.components.diplomacy;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Player;
import sr.world.diplomacy.DiplomacyState;

/**
 * Contains the diplomacy rows. This panel should be put inside a scrollpane.
 * @author WMPABOD
 *
 */
public class DiplomacyRowsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<DiplomacyRow> rows;
	private int preferredHeight = 2;

	public DiplomacyRowsPanel(Player aPlayer, DiplomacyPanel diplomacyPanel, boolean activePlayer){
		setLayout(null);
		setBackground(Color.BLACK);
		setOpaque(true);
		rows = new LinkedList<DiplomacyRow>();
		Galaxy g = aPlayer.getGalaxy();
		List<DiplomacyState> states = g.getDiplomacy().getDiplomacyStates(aPlayer);
		for (DiplomacyState state : states) {
			Logger.finest("state: " + state.toString());
			Player otherPlayer = state.getOtherPlayer(aPlayer);
//			System.out.println(aPlayer.getGovenorName() + otherPlayer.getGovenorName());
			DiplomacyRow tmpRow = new DiplomacyRow(state,otherPlayer,diplomacyPanel,activePlayer);
			tmpRow.setBounds(2, preferredHeight, 480, 32);
			rows.add(tmpRow);
			add(tmpRow);
			preferredHeight += 32;
		}
		/* testa scroller
		for (int i = 1; i < 15; i++){
			DiplomacyState state = states.get(0);
			Player otherPlayer = state.getOtherPlayer(aPlayer);
//			System.out.println(aPlayer.getGovenorName() + otherPlayer.getGovenorName());
			DiplomacyRow tmpRow = new DiplomacyRow(state,otherPlayer);
			tmpRow.setBounds(2, preferredHeight, 480, 32);
			rows.add(tmpRow);
			add(tmpRow);
			preferredHeight += 32;
		}
		*/
		setSize(484, preferredHeight+2);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(480,preferredHeight);
	}
}
