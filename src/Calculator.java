import java.util.Scanner;
import java.util.Stack;

public class Calculator
{
    // Scanner that reads user input
    private static final Scanner scanner = new Scanner(System.in);

    // String builder to form new numbers
    private final StringBuilder numberBuilder = new StringBuilder();

    // Stack of operands
    private final Stack<CalculatorNodes> operandStack = new Stack<>();
    // Stack of operators
    private final Stack<String> operatorStack = new Stack<>();

    // Flag to form decimal numbers.
    private boolean isDotPresent = false;

    // Main method
    public static void main(String[] args)
    {
        Calculator calculator = new Calculator();
        String expression = getStringUser();
        CalculatorNodes expressionTree = calculator.parseExpression(expression);
        double result = expressionTree.evaluate();
        System.out.println("The result is: " + result);
    }

    // Method to ask the user for input
    public static String getStringUser()
    {
        System.out.println("Type a mathematical operation:");
        return scanner.nextLine();
    }

    // Analyze the numberBuilder and handle the last number in the string and reset flag
    public void updateNumberBuilder()
    {
        // If the number builder have digits, push them into the operandStack as a String, reset its length to 0
        if (numberBuilder.length() > 0) {
            operandStack.push(new CalculatorNodes(numberBuilder.toString()));
            numberBuilder.setLength(0);
            isDotPresent = false; // Reset for the next number
        }
    }

    // Pop the last value in operatorStack, the last 2 operands in operandStack,
    // and pass those arguments into a new node into operandStack
    public void updateStacks()
    {
        String operator = operatorStack.pop(); // Put the value of the last operator in stack into
        // a String to be passed as an argument for the next node
        CalculatorNodes rightOperand = operandStack.pop(); // Pop last operand into a node that will be the right child
        CalculatorNodes leftOperand = operandStack.pop(); // Pop last operand into a node that will be the left child
        operandStack.push(new CalculatorNodes(operator, leftOperand, rightOperand)); // Create a new node with arguments
    }

    // Analyze the current character if it's a digit or a decimal point and add them to the numberBuilder
    public void analyzeDigit(char currentChar)
    {
        if (currentChar == '.' && !isDotPresent)
        {
            isDotPresent = true;
            numberBuilder.append(currentChar); // Add to the number builder to create decimals
        } else if (Character.isDigit(currentChar))
        {
            numberBuilder.append(currentChar);
        }
    }

    // Handle operator analysis and precedence
    public void analyzeOperator(char currentChar)
    {
        updateNumberBuilder(); // Add the current digits value into an operand node, into the operand stack
        while (!operatorStack.isEmpty() && precedence(operatorStack.peek())
                >= precedence(Character.toString(currentChar))) // Until operatorStack is
            // empty and the level of precedence of the last operator in stack is lesser than the current operator
        {
            updateStacks();
        }
        operatorStack.push(Character.toString(currentChar)); // Push the operator into the operator list
    }

    // Handle opening parenthesis
    public void analyzeOpeningParenthesis(char previousChar)
    {
        updateNumberBuilder(); // Add the current digits value into an operand node, into the operand stack
        // If the previous character is a digit, or a closing parenthesis, add * into the operator list to act as a multiplication
        if (Character.isDigit(previousChar) || previousChar == ')')
        {
            operatorStack.push("*");
        }
        operatorStack.push("(");
    }

    // Handle closing parenthesis
    public void analyzeClosingParenthesis()
    {
        updateNumberBuilder(); // Add the current digits value into an operand node, into the operand stack
        while (!operatorStack.peek().equals("(")) // Until the operator "(" is the last in stack
        {
            updateStacks();
        }
        operatorStack.pop(); // Remove the ( from the stack
    }

    // Define what is an operator
    private boolean isOperator(char character)
    {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    // Determine operator precedence
    private int precedence(String operator)
    {
        switch (operator)
        {
            // Addition and subtraction are 1 because multiplication and division have priority over them, those are 2
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    // Parse the expression into an expression tree
    public CalculatorNodes parseExpression(String expression)
    {
        int length = expression.length();

        for (int i = 0; i < length; i++)
        {
            char currentChar = expression.charAt(i); // The current character of the string provided by the user
            char previousChar; // The previous character of the string provided by the user
            if (i > 0)
            {
                previousChar = expression.charAt(i - 1);
            }
            else
            {
                previousChar = '\0'; // '\0' is a null character, used here to represent no previous character
            }

            if (Character.isDigit(currentChar) || currentChar == '.')
            {
                analyzeDigit(currentChar);
            }
            else if (isOperator(currentChar))
            {
                analyzeOperator(currentChar);
            }
            else if (currentChar == '(')
            {
                analyzeOpeningParenthesis(previousChar);
            }
            else if (currentChar == ')')
            {
                analyzeClosingParenthesis();
            }
        }

        // Handle the last number in the string
        updateNumberBuilder();

        // Process remaining operators in the stack
        while (!operatorStack.isEmpty())
        {
            updateStacks();
        }

        // The final tree is in the operand stack
        return operandStack.pop();
    }
}
