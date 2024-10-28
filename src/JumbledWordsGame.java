import java.util.Random;
import java.util.Scanner;

class Category {
    String[] questions;
    String[] answers;

    public Category(String[] questions, String[] answers) {
        this.questions = questions;
        this.answers = answers;
    }
}

public class JumbledWordsGame {

    public static void shuffleString(StringBuilder str) {
        Random random = new Random();
        for (int i = str.length() - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = str.charAt(i);
            str.setCharAt(i, str.charAt(j));
            str.setCharAt(j, temp);
        }
    }

    public static int correctOrIncorrect(String ques, String ans) {
        return ques.equalsIgnoreCase(ans) ? 0 : 1;
    }

    public static int gameRound(Category userCategory, int quesIndex, Scanner scanner) {
        StringBuilder question = new StringBuilder(userCategory.questions[quesIndex]);
        shuffleString(question);

        System.out.println("\nQuestion: " + question);
        System.out.print("Answer: ");
        String answer = scanner.next().toUpperCase();

        return correctOrIncorrect(userCategory.answers[quesIndex], answer);
    }

    public static void main(String[] args) {
        // Test data for the game logic
        Category testCategory = new Category(
                new String[]{"APPLE", "BANANA", "CHERRY", "MANGO", "GRAPES"},
                new String[]{"APPLE", "BANANA", "CHERRY", "MANGO", "GRAPES"}
        );

        System.out.println("\n----JUMBLED WORDS GAME (TEST)----\n");

        Scanner scanner = new Scanner(System.in);

        // Main gameplay function
        int score = 0;
        int life = 3;

        // Array for the question indices
        int[] quesIndexArray = {0, 1, 2, 3, 4};

        // Shuffle the question indices
        Random random = new Random();
        for (int i = quesIndexArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = quesIndexArray[i];
            quesIndexArray[i] = quesIndexArray[j];
            quesIndexArray[j] = temp;
        }

        // Game loop
        int i = 0;
        while (score < 5 && life > 0 && i < quesIndexArray.length) {
            int quesIndex = quesIndexArray[i];
            int result = gameRound(testCategory, quesIndex, scanner);

            if (result == 0) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect!");
                life--;
            }

            System.out.printf("Score: %d, Life: %d%n%n", score, life);
            i++;
        }

        if (score == 5) {
            System.out.println("Congratulations! You Won The Game");
        } else {
            System.out.println("Game Over. Better Luck Next Time!");
        }

        scanner.close();
    }
}
