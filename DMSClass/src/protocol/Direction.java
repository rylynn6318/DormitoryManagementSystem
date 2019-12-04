package protocol;

public enum Direction{
	TO_SERVER, TO_CLIENT;
	
	public static Direction getDirection(int x) {
		switch (x) {
		case 0:
			return TO_SERVER;
		case 1:
			return TO_CLIENT;
		}
		return null;
	}
}
