import java.util.*;

/* ================= MAIN CLASS ================= */
public class QuizGame {

    private static Map<String, Quiz> quizzes = new HashMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter a command (create, take, view, list, exit):");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "create":
                    createQuiz(scanner);
                    break;
                case "take":
                    takeQuiz(scanner);
                    break;
                case "view":
                    viewQuiz(scanner);
                    break;
                case "list":
                    listQuizzes();
                    break;
                case "exit":
                    System.out.println("Exiting quiz game...");
                    return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    /* ================= CREATE QUIZ ================= */
    private static void createQuiz(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        Quiz quiz = new Quiz(quizName);

        int numQuestions = getValidatedInteger(scanner, "Enter number of questions:");

        for (int i = 0; i < numQuestions; i++) {
            System.out.print("Enter question: ");
            String questionText = scanner.nextLine();

            int numChoices = getValidatedInteger(scanner, "Enter number of choices:");
            List<String> choices = new ArrayList<>();

            for (int j = 0; j < numChoices; j++) {
                System.out.print("Choice " + (j + 1) + ": ");
                choices.add(scanner.nextLine());
            }

            int correctChoice;
            while (true) {
                correctChoice = getValidatedInteger(scanner,
                        "Enter correct choice number (1 to " + numChoices + "):") - 1;
                if (correctChoice >= 0 && correctChoice < numChoices)
                    break;
                System.out.println("Invalid choice index.");
            }

            quiz.addQuestion(new Question(questionText, choices, correctChoice));
        }

        quizzes.put(quizName, quiz);
        System.out.println("Quiz created successfully!");
    }

    /* ================= TAKE QUIZ ================= */
    private static void takeQuiz(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        Quiz quiz = quizzes.get(quizName);
        if (quiz == null) {
            System.out.println("Quiz not found.");
            return;
        }

        int score = 0;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < quiz.getNumQuestions(); i++) {
            Question q = quiz.getQuestion(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestion());

            List<String> choices = q.getChoices();
            for (int j = 0; j < choices.size(); j++) {
                System.out.println((j + 1) + ". " + choices.get(j));
            }

            int userAnswer;
            while (true) {
                try {
                    System.out.print("Enter your answer: ");
                    userAnswer = Integer.parseInt(scanner.nextLine()) - 1;
                    if (userAnswer >= 0 && userAnswer < choices.size())
                        break;
                    System.out.println("Invalid option.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                }
            }

            if (userAnswer == q.getCorrectChoice()) {
                System.out.println("✔ Correct");
                score++;
            } else {
                System.out.println("✘ Incorrect. Correct answer: "
                        + choices.get(q.getCorrectChoice()));
            }
        }

        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startTime) / 1000;

        System.out.println("\nQuiz Completed!");
        System.out.println("Score: " + score + "/" + quiz.getNumQuestions());
        System.out.println("Time Taken: " + seconds + " seconds");
    }

    /* ================= VIEW QUIZ ================= */
    private static void viewQuiz(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        Quiz quiz = quizzes.get(quizName);
        if (quiz == null) {
            System.out.println("Quiz not found.");
            return;
        }

        for (int i = 0; i < quiz.getNumQuestions(); i++) {
            Question q = quiz.getQuestion(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestion());

            List<String> choices = q.getChoices();
            for (int j = 0; j < choices.size(); j++) {
                System.out.println((j + 1) + ". " + choices.get(j));
            }

            System.out.println("Answer: " + choices.get(q.getCorrectChoice()));
        }
    }

    /* ================= LIST QUIZZES ================= */
    private static void listQuizzes() {
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available.");
            return;
        }

        System.out.println("Available Quizzes:");
        for (String name : quizzes.keySet()) {
            System.out.println("- " + name);
        }
    }

    /* ================= VALIDATION ================= */
    private static int getValidatedInteger(Scanner scanner, String message) {
        int value;
        while (true) {
            try {
                System.out.print(message + " ");
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0)
                    return value;
                System.out.println("Enter number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }
}

/* ================= QUIZ CLASS ================= */
class Quiz {
    private String name;
    private List<Question> questions = new ArrayList<>();

    public Quiz(String name) {
        this.name = name;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int getNumQuestions() {
        return questions.size();
    }
}

/* ================= QUESTION CLASS ================= */
class Question {
    private String question;
    private List<String> choices;
    private int correctChoice;

    public Question(String question, List<String> choices, int correctChoice) {
        this.question = question;
        this.choices = choices;
        this.correctChoice = correctChoice;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectChoice() {
        return correctChoice;
    }
}
