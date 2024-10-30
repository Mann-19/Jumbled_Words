import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Custom exception to handle invalid category input
class InvalidCategoryException extends Exception {
    public InvalidCategoryException(String message) {
        super(message);
    }
}

class Category {
    List<String> questions;
    List<String> answers;

    public Category(List<String> questions, List<String> answers) {
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
        StringBuilder question = new StringBuilder(userCategory.questions.get(quesIndex));
        shuffleString(question);

        System.out.println("\nQuestion: " + question);
        System.out.print("Answer: ");
        String answer = scanner.next().toUpperCase();

        return correctOrIncorrect(userCategory.answers.get(quesIndex), answer);
    }

    public static Category loadCategoryFromFile(String category) throws InvalidCategoryException {
        String filePath = "categories/" + category + ".txt";
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isQuestion = true;
            while ((line = reader.readLine()) != null) {
                if (isQuestion) {
                    questions.add(line.trim());
                } else {
                    answers.add(line.trim());
                }
                isQuestion = !isQuestion;
            }

            if (questions.size() != answers.size()) {
                throw new InvalidCategoryException("Invalid file format in " + category + ".txt");
            }

        } catch (FileNotFoundException e) {
            throw new InvalidCategoryException("Invalid category. Please enter a valid category.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Category(questions, answers);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Category selectedCategory = null;

        // Loop until user enters a valid category
        while (selectedCategory == null) {
            System.out.println("Choose a category: animals, foods, fruits, anime, cartoons, marvel, sports");
            System.out.print("Enter a category: ");
            String category = scanner.next().toLowerCase();

            try {
                selectedCategory = loadCategoryFromFile(category);
            } catch (InvalidCategoryException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\n----JUMBLED WORDS GAME----\n");

        int score = 0;
        int life = 3;

        // Shuffle question indices
        Random random = new Random();
        List<Integer> quesIndexArray = new ArrayList<>();
        for (int i = 0; i < selectedCategory.questions.size(); i++) {
            quesIndexArray.add(i);
        }
        for (int i = quesIndexArray.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = quesIndexArray.get(i);
            quesIndexArray.set(i, quesIndexArray.get(j));
            quesIndexArray.set(j, temp);
        }

        // Game loop
        int i = 0;
        while (score < 10 && life > 0 && i < quesIndexArray.size()) {
            int quesIndex = quesIndexArray.get(i);
            int result = gameRound(selectedCategory, quesIndex, scanner);

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

        if (score == 10) {
            System.out.println("Congratulations! You Won The Game");
        } else {
            System.out.println("Game Over. Better Luck Next Time!");
        }

        scanner.close();
    }
}
