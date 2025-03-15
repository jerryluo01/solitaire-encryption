package assignment2;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	/*
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {

		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfSuits > 4) {
			throw new IllegalArgumentException("Invalid inputs");
		}

		numOfCards = numOfCardsPerSuit * numOfSuits + 2;
		Card prev = null;
		Card redJoker = new Joker("red");
		Card blackJoker = new Joker("black");

		for (int i = 0; i < numOfSuits; i++) {
			for (int j = 1; j <= numOfCardsPerSuit; j++) {
				Card newCard = new PlayingCard(suitsInOrder[i], j);

				if (head == null) {
					head = newCard;
				} else {
					prev.next = newCard;		//CHECK THIS EXCEPTION
					newCard.prev = prev;
				}
				prev = newCard;
			}
		}
		prev.next = redJoker;
		redJoker.prev = prev;
		redJoker.next = blackJoker;
		blackJoker.prev = redJoker;
		blackJoker.next = head;
		head.prev = blackJoker;
	}

	/*
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		if (d.head == null) return;
		this.numOfCards = d.numOfCards;
		this.head = d.head.getCopy();
		Card cur = this.head;
		Card sourceCard = d.head.next;

		while (sourceCard != d.head) {
			Card copiedCard = sourceCard.getCopy();
			cur.next = copiedCard;
			copiedCard.prev = cur;
			cur = copiedCard;
			sourceCard = sourceCard.next;
		}
		cur.next = head;
		head.prev = cur;
	}

	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {}

	/*
	 * TODO: Adds the specified card at the bottom of the deck. This
	 * method runs in $O(1)$.
	 */
	public void addCard(Card c) {
		if (head == null) {
			head = c;
			head.prev = c;
			head.next = c;
		} else {
			Card lastCard = head.prev;
			lastCard.next = c;
			c.next = head;
			c.prev = lastCard;
			head.prev = c;
		}
		this.numOfCards++;
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf.
	 * This method runs in O(n) and uses O(n) space, where n is the total
	 * number of cards in the deck.
	 */
	public void shuffle() {
		if (this.head == null || numOfCards <= 1) return;

		Card[] deckArray= new Card[numOfCards];
		Card cur = this.head;

		for (int i = 0; i < numOfCards; i++) {
			deckArray[i] = cur;
			cur = cur.next;
		}
		for (int i = numOfCards - 1; i > 0; i--) {
			int j = gen.nextInt(i + 1);
			Card temp = deckArray[i];
			deckArray[i] = deckArray[j];
			deckArray[j] = temp;

		}

		for (int i = 0; i < numOfCards; i++) {
			deckArray[i].next = deckArray[(i + 1) % numOfCards];
			deckArray[i].prev = deckArray[(i - 1 + numOfCards) % numOfCards];
		}
		this.head = deckArray[0];
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in
	 * the deck. This method runs in O(n), where n is the total number of
	 * cards in the deck.
	 */
	public Joker locateJoker(String color) {
		if (this.head == null || color == null) return null;

		Card cur = this.head;

		do {
			if (cur instanceof Joker) {
				Joker joker = (Joker) cur;
				if (joker.redOrBlack.equalsIgnoreCase(color)) {
					return joker;
				}
			}
			cur = cur.next;
		} while (cur != head);

		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		if (numOfCards <= 1 || p == 0 || c == null) return;

		c.next.prev = c.prev;
		c.prev.next = c.next;

		Card newPosition = c;
		for (int i = 0; i < p; i++) {
			newPosition = newPosition.next;
		}

		c.next = newPosition.next;
		c.prev = newPosition;
		newPosition.next.prev = c;
		newPosition.next = c;

		if (head == c) {
			head = head.next;
		}
	}

	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You
	 * can assume that the input cards belong to the deck and the first one is
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {
		if (firstCard == null || head == null || firstCard == secondCard) return;

		Card topStart = head;
		Card topEnd = firstCard.prev;
		Card botStart = secondCard.next;
		Card botEnd = head.prev;

		if (firstCard.prev == secondCard) return;

		if (topStart != firstCard) {
			if (secondCard.next == topStart) {
				head = firstCard;
			} else {
				firstCard.prev = botEnd;
				topEnd.next = botStart;
				botStart.prev = topEnd;
				botEnd.next = firstCard;

				secondCard.next = topStart;
				topStart.prev = secondCard.next;

				head = botStart;
			}
		} else {
			head = botStart;
		}
	}



	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the
	 * bottom card is equal to a multiple of the number of cards in the deck,
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		if (this.head == null || numOfCards <= 1) return;

		int cutNum = this.head.prev.getValue();
		cutNum %= numOfCards;

		if (cutNum == 0 || cutNum == numOfCards - 1) return;

		Card cur = head;
		for (int i = 1; i < cutNum; i++) {
			cur = cur.next;
		}

		Card botEnd = head.prev.prev;
		Card topStart = head;
		Card botStart = cur.next;
		Card lastCard = head.prev;

		lastCard.next = botStart;
		topStart.prev = botEnd;
		botStart.prev = lastCard;
		botEnd.next = topStart;

		head = botStart;
	}


	/*
	 * TODO: Returns the card that can be found by looking at the value of the
	 * card on the top of the deck, and counting down that many cards. If the
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		Card cur = this.head;
		int value = cur.getValue();

		for (int i = 0; i < value; i++) {
			cur = cur.next;
		}
		if (!(cur instanceof Joker)) {
			return cur;
		}
		return null;
	}

	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		if (head == null || numOfCards <= 1) return -1;

		Joker firstJoker = null;
		Joker secondJoker = null;

		Joker redJoker = locateJoker("red");
		if (redJoker == null) return -1;
		moveCard(redJoker, 1);

		if (redJoker.next == this.head) {  //unecessary?
			moveCard(redJoker, 1);
		}

		Joker blackJoker = locateJoker("black");
		if (blackJoker == null) return -1;
		moveCard(blackJoker, 2);
		if (blackJoker.next == this.head) {  //unecessary?
			moveCard(blackJoker, 2);
		} else if (blackJoker.next.next == this.head) { //unecessary?
			moveCard(blackJoker, 2);
		}

		Card cur = this.head;

		for (int i = 0; i < numOfCards; i++) {
			if (cur instanceof Joker) {
				Joker joker = (Joker) cur;
				if (firstJoker == null) {
					firstJoker = joker;
				} else {
					secondJoker = joker;
					break;
				}
			}
		}

		tripleCut(firstJoker, secondJoker);
		countCut();
		Card value = lookUpCard();

		if (value == null) {
			generateNextKeystreamValue();
		} else return value.getValue();

		return 0;
	}


	public abstract class Card {
		public Card next;
		public Card prev;

		public abstract Card getCopy();
		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				//info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = {"Jack", "Queen", "King"};
				//info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			//info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13*i;
		}

	}

	public class Joker extends Card{
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
				throw new IllegalArgumentException("Jokers can only be red or black");

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			//return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

	public static void main(String[] args) {
		int[] keystream;

		Deck deck = new Deck(5, 2);
		gen.setSeed(10);
		deck.shuffle();
		SolitaireCipher cipher = new SolitaireCipher(deck);  // RECHK IF THATS HOW U GENERATE KEYSTREAM
		keystream = cipher.getKeystream(12);
        for (int j : keystream) {
            System.out.print(j + "	");
        }
		String encoded = cipher.encode("Is that you, Bob?");
		System.out.println("\n" + encoded);

		/* Deck deck = new Deck(5, 2);
		gen.setSeed(10);
		deck.shuffle();
		Card cur = deck.head;
		for (int i = 0; i < deck.numOfCards; i++) {
			System.out.print(cur + "	");
			cur = cur.next;
		}

		System.out.println();

		deck.countCut();

		Card cur1 = deck.head;
		for (int j = 0; j < deck.numOfCards; j++) {
			System.out.print(cur1 + "	");
			cur1 = cur1.next;
		} */
	}
}