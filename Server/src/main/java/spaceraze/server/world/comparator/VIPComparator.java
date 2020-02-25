package sr.world.comparator;

import java.util.Comparator;

import sr.world.VIP;

/**
 * Sort order for VIPs:
 * 
 * 1. Governor
 * 2. Duellists
 * 3. Name alfabetically
 * 
 * @author Paul Bodin
 */
public class VIPComparator implements Comparator<VIP> {

	public int compare(VIP vip1, VIP vip2) {
		int diff = 0;
		if (vip1.isGovernor()){ 
			diff = -1;
		}else
		if (vip2.isGovernor()){
			diff = 1;
		}else
		if (vip1.isDuellist() & !vip2.isDuellist()){
				diff = -1;
		}else
		if (!vip1.isDuellist() & vip2.isDuellist()){
				diff = 1;
		}else{
			diff = vip1.getName().compareToIgnoreCase(vip2.getName());
		}
		return diff;
	}

}
