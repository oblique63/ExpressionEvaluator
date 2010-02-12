package evaluator;

import java.util.*;
import java.util.regex.*;

public class Evaluator {
    //----{ Constructor + Private Variables }-----------------------------------
    private static Stack<Operand> opdStack;
    private static Stack<Operator> oprStack;
    public Evaluator() {
        opdStack = new Stack<Operand>();
        oprStack = new Stack<Operator>();
    }

    //----{ Operand/Operator Classes }------------------------------------------
    static class Operand {
        private int value;
        Operand(String toekn) {
            value = Integer.parseInt(toekn);
        }
        Operand(int inputValue) {
            value = inputValue;
        }
        static boolean check(String token) {
            return Pattern.matches("[0-9]+", token);
        }
        int getValue() {
            return value;
        }
    }

    abstract static class Operator {
        abstract int priority();
        abstract Operand execute(Operand opd1, Operand opd2);
        static boolean check(String token) {
        	return operators.containsKey(token);
        }
        private static String avalibleOperators() {
            /** Returns a string with all the defined operators **/
        	String oprs = "";
            for (String operator : operators.keySet())
                oprs += operator;
            return oprs;
        }
        private static HashMap<String, Operator> setupOperatorMap() {
        	/** Instantiates hash map of all the defined operators **/
            HashMap<String, Operator> operatorMap = new HashMap<String, Operator>();
            operatorMap.put("+", new AdditionOperator());
            operatorMap.put("-", new SubtractionOperator());
            operatorMap.put("*", new MultiplicationOperator());
            operatorMap.put("/", new DivisionOperator());
            operatorMap.put("#", new PoundOperator());
            operatorMap.put("!", new BangOperator());
            /* Add new operators here */

            return operatorMap;
        }
        private static HashMap<String, Operator> operators = setupOperatorMap();
    }

    static class PoundOperator extends Operator {
        int priority() {
            return 0;
        }
        Operand execute(Operand opd1, Operand opd2) {
            return opd1;
        }

    }

    static class BangOperator extends Operator {
        int priority() {
            return 1;
        }
        Operand execute(Operand opd1, Operand opd2) {
            return opd1;
        }

    }

    static class AdditionOperator extends Operator {
        int priority() {
            return 2;
        }
        Operand execute(Operand opd1, Operand opd2) {
            int sum = opd1.getValue() + opd2.getValue();
            return new Operand(sum);
        }

    }

    static class SubtractionOperator extends Operator {
        int priority() {
            return 2;
        }
        Operand execute(Operand opd1, Operand opd2) {
            int diff = opd1.getValue() - opd2.getValue();
            return new Operand(diff);
        }
    }

    static class MultiplicationOperator extends Operator {
        int priority() {
            return 3;
        }
        Operand execute(Operand opd1, Operand opd2) {
            int prod = opd1.getValue() * opd2.getValue();
            return new Operand(prod);
        }
    }

    static class DivisionOperator extends Operator {
        int priority() {
            return 3;
        }
        Operand execute(Operand opd1, Operand opd2) {
            int quo = opd1.getValue() / opd2.getValue();
            return new Operand(quo);
        }
    }

    //----{ Evaluator Method }--------------------------------------------------
    public int eval(String expression) {
    	String token;
        oprStack.push(Operator.operators.get("#"));
        StringTokenizer st = new StringTokenizer(expression+"!", Operator.avalibleOperators()+" ", true);
        while (st.hasMoreTokens()) {
            if (!(token = st.nextToken()).equals(" ")) {
                if (Operand.check(token)) {
                    opdStack.push(new Operand(token));
                } else {
                    if (!Operator.check(token)) {
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }
                    Operator newOpr = Operator.operators.get(token);
                    while (oprStack.peek().priority() >= newOpr.priority()) {
                        Operator oldOpr = oprStack.pop();
                        Operand op2 = opdStack.pop();
                        Operand op1 = opdStack.pop();
                        opdStack.push(oldOpr.execute(op1, op2));
                    }
                    oprStack.push(newOpr);
                }
            }
        }
        return opdStack.pop().getValue();
    }
        
}