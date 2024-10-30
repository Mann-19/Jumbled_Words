import java.util.List;

public class Category {
    public List<String> questions;
    public List<String> answers;

    public Category(List<String> questions, List<String> answers) {
        this.questions = questions;
        this.answers = answers;
    }
}
