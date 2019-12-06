package enums;

public enum Grade {
	APLUS("A+", (byte) 0x00), A("A", (byte) 0x01), BPLUS("B+", (byte) 0x02), B("B", (byte) 0x03),
	CPLUS("C+", (byte) 0x04), C("C", (byte) 0x05), DPLUS("D+", (byte) 0x06), D("D", (byte) 0x07), F("F", (byte) 0x08);

	public final String grade;
	public final byte bit;

	Grade(String grade, byte bit) {
		this.grade = grade;
		this.bit = bit;
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
