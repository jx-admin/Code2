
public class Main {
	static int lang = 4;

	public static void main(String argv[]) {
		switch (lang) {
		case Language.java:
			System.out.println("java");
			break;
		case Language.pascal:
			System.out.println("pascal");
			break;
		case Language.csharp:
			System.out.println("csharp");
			break;
		default:
			assert false : lang;
		}
	}
}

class Language {
	public static final int java = 1;
	public static final int pascal = 2;
	public static final int csharp = 3;
}

