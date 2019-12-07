package enums;

public enum Grade {
	APLUS("A+"), A("A"), BPLUS("B+"), B("B"), CPLUS("C+"), C("C"), DPLUS("D+"), D("D"), F("F");

	public final String grade;

	Grade(String grade) {
		this.grade = grade;
	}

	public static Grade get(String grade) {
		switch (grade) {
		case "A+":
			return APLUS;
		case "A":
			return A;
		case "B+":
			return BPLUS;
		case "B":
			return B;
		case "C+":
			return CPLUS;
		case "C":
			return C;
		case "D+":
			return DPLUS;
		case "D":
			return D;
		case "F":
			return F;
		default:
			return null;
		}
	}
}
