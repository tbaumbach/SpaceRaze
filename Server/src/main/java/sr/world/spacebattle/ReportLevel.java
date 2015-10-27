package sr.world.spacebattle;

public enum ReportLevel {
	SHORT("Short"),
	MEDIUM("Medium"),
	LONG("Detailed");

	private String desc;
	
	private ReportLevel(String desc){
		this.desc = desc;
	}
	
	public String toString(){
		return desc;
	}
	
	public static ReportLevel getReportLevel(String text){
		ReportLevel foundReportLevel = null;
		ReportLevel[] levels = ReportLevel.values();
		int index = 0;
		while ((foundReportLevel == null) & (index < levels.length)){
			ReportLevel aReportLevel = levels[index];
			if (aReportLevel.toString().equals(text)){
				foundReportLevel = aReportLevel;
			}else{
				index++;
			}
		}
		return foundReportLevel;
	}
}
