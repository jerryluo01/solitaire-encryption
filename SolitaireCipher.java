package assignment2;

public class SolitaireCipher {
	public Deck key;

	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		if (size == 0) return new int[0];
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		int errcode;

		int[] keystream = new int[size];

		for (int i = 0; i < size; i++) {
			keystream[i] = key.generateNextKeystreamValue();
			errcode = keystream[i];
			if (errcode == -1) {
				throw new NullPointerException();
				}
		}
		return keystream;
	}

	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		if (msg == null) return null;

		String stripped = msg.replaceAll("[^a-zA-Z]", "");
		if (stripped.isEmpty()) return "";
		String upper = stripped.toUpperCase();
		int length = upper.length();
		int[] keystream = getKeystream(length);
		String result = "";

		for (int i = 0; i < length; i++) {
			char ch = upper.charAt(i);
			char firstLetter = 'A';
			ch = (char) (firstLetter + (ch - firstLetter + keystream[i]) % 26);
			result += ch;
		}
		return result;
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
    public String decode(String msg) {
        if (msg == null) {
            return null;
        }

        msg = msg.replaceAll("[^a-zA-Z]", "").toUpperCase();

        if (msg.isEmpty()) {
            return "";
        }

        int[] keystream = getKeystream(msg.length());

        char[] decodedChars = new char[msg.length()];
        for (int i = 0; i < msg.length(); i++) {
            int charValue = msg.charAt(i) - 'A'; 
            int decodedValue = (charValue - keystream[i]) % 26; 
            if (decodedValue < 0) {
                decodedValue += 26; 
            }
            char decodedChar = (char) (decodedValue + 'A'); 
            decodedChars[i] = decodedChar;
        }

        return new String(decodedChars);
    }
}
