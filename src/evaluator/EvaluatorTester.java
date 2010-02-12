package evaluator;

public class EvaluatorTester {
    public static void main(String[] args) {
        Evaluator anEvaluator = new Evaluator();
        for (String expression : args)
            System.out.println(expression + " = " + anEvaluator.eval(expression));
    }
}
