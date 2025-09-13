# Solitaire Encryption (Pontifex Cipher)

An implementation of the **Solitaire (Pontifex) Cipher** in Java, using a deck of cards to generate a pseudo-random keystream for encryption and decryption.  
This project demonstrates both the **cipher algorithm** and supporting data structures, such as a **doubly linked list** to model the deck and the **Fisher–Yates shuffle** for randomized deck initialization.

---

## 🔑 About the Cipher
The Solitaire (Pontifex) cipher was designed by **Bruce Schneier** and popularized in Neal Stephenson’s novel *Cryptonomicon*.  
It is notable for being a **paper-and-pencil cipher** that can be executed using a standard deck of playing cards, yet still provides strong cryptographic properties.

---

## ✨ Features
- **Encryption & Decryption**: Implements the full Solitaire algorithm to convert plaintext into ciphertext and vice versa.
- **Deck Representation**: Uses a doubly linked list for efficient card movement operations (cutting, moving jokers, etc.).
- **Fisher–Yates Shuffle**: Initializes a randomized deck for keystream generation.
- **Object-Oriented Design**: Separation of concerns between `Deck.java` (deck management) and `SolitaireCipher.java` (cipher operations).

---

## 📂 Project Structure
- `Deck.java` → Handles deck operations, shuffling, and card manipulation.  
- `SolitaireCipher.java` → Implements the encryption/decryption logic.  
- `README.md` → Project documentation.  

---

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher

### Installation & Usage
1. Clone the repository:
   ```bash
   git clone https://github.com/jerryluo01/solitaire-encryption.git
