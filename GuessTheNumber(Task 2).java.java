import javax.swing.JOptionPane;
import java.util.Random;
class Game {
    public static final int MAX_ATTEMPTS = 10; 
    private int number;
    private int inputNumber;
    private int noOfGuesses = 0;
    private int attemptsLeft = MAX_ATTEMPTS;
    Game() {
        Random rand = new Random();
        this.number = rand.nextInt(100) + 1; 
    }
    void takeUserInput() {
        String input = JOptionPane.showInputDialog("Guess the number (between 1 and 100):");
        try {
            inputNumber = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.");
            inputNumber = -1; 
        }
    }
    boolean isCorrectNumber() {
        noOfGuesses++;
        attemptsLeft--;
        if (inputNumber == number) {
            JOptionPane.showMessageDialog(null, String.format("Congratulations! You guessed it right, it was %d\nYou guessed it in %d attempts", number, noOfGuesses));
            return true;
        } else if (inputNumber < number) {
            JOptionPane.showMessageDialog(null, "Too low...");
        } else {
            JOptionPane.showMessageDialog(null, "Too high...");
        }
        if (attemptsLeft > 0) {
            JOptionPane.showMessageDialog(null, String.format("You have %d attempts left.", attemptsLeft));
        } else {
            JOptionPane.showMessageDialog(null, String.format("Sorry, you've used all your attempts. The number was %d.", number));
        }
        return false;
    }
    int getNoOfGuesses() {
        return noOfGuesses;
    }
    void resetGame() {
        Random rand = new Random();
        this.number = rand.nextInt(100) + 1;
        this.noOfGuesses = 0;
        this.attemptsLeft = MAX_ATTEMPTS;
    }
    int getInputNumber() {
        return inputNumber;
    }
}
public class GuessTheNumber {
    public static void main(String[] args) {
        Game game = new Game();
        boolean roundComplete = false;
        while (!roundComplete && game.getNoOfGuesses() < Game.MAX_ATTEMPTS) {
            game.takeUserInput();
            if (game.getInputNumber() != -1) { // Check if input was valid
                roundComplete = game.isCorrectNumber();
            }
        }
        int score = Math.max(0, 100 - 10 * (game.getNoOfGuesses() - 1));
        JOptionPane.showMessageDialog(null, String.format("Your score for this round is: %d", score));

        JOptionPane.showMessageDialog(null, String.format("Game Over! You guessed %d times.", game.getNoOfGuesses()));
    }
}
