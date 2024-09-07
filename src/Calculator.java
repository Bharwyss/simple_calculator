import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Calculator
{
    /** Fields */
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

    /** Methods */
    // Main method
    public static void main(String[] args)
    {
        try {
            Calculator calculator = new Calculator(); // Create a calculator object
            String expression = getStringUser(); // Put input from the user in a String called expression
            CalculatorNodes expressionTree = calculator.parseExpression(expression); // Parse the expression
            double result = expressionTree.evaluateExpressionTree(); // Evaluate the expression once the tree has been made
            System.out.println("The result is: " + result); // Print the result
        }
        catch (IllegalArgumentException e) // Catch specific errors related to invalid arguments (e.g., missing closing parenthesis)
        {
            System.out.println(e.getMessage()); // Display the specific error message provided by the exception
        }
        catch (Exception e) // Catch any other unexpected errors
        {
            System.out.println("An unexpected error occurred: " + e.getMessage()); // Display a general error message with details
        }
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
        if (numberBuilder.length() > 0)
        {
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
        operandStack.push(new CalculatorNodes(operator, leftOperand, rightOperand)); // Create a new node with previous arguments
    }

    // Handle digits and decimal points analysis and the numberBuilder construction
    public void analyzeDecimalNumber(char currentChar)
    {
        if (currentChar == '.' && !isDotPresent)
        {
            isDotPresent = true;
            numberBuilder.append(currentChar); // Add to the number builder to create decimals
        }
        else if (Character.isDigit(currentChar))
        {
            numberBuilder.append(currentChar);
        }
    }

    // Handle operator analysis and the operator stack flow
    public void analyzeOperator(char currentChar, char previousChar)
    {
        updateNumberBuilder(); // Add the current digits value stored of numberBuilder into an operand node, into the operand stack

        // if the operator is '-' and if the previous character is an operator or '(',
        // or if the previous character is null, treat it as part of the on forming operand
        if (currentChar == '-' && (isOperator(previousChar) || previousChar == '(' || previousChar == '\0'))
        {
            numberBuilder.append(currentChar); // Add '-' to the numberBuilder
            return; // Exit early since we're treating this as part of a number, not as an operator
        }

        // Push current char in the operatorStack if the stack is empty. While not and if the precedence of
        // the current operator analyzed is greater or equal than the one compared to the last one in the stack, update stacks.
        if (!isOperator(previousChar)) // If the previous character isn't an operator
        {
            while (!operatorStack.isEmpty() && precedence(operatorStack.peek())
                    >= precedence(Character.toString(currentChar))) // Until operatorStack is
            // empty and the level of precedence of the last operator in stack is lesser than the current operator
            {
                updateStacks(); // Convert the last element of operatorStack as a string to pass value, pop the last two
                // elements of the operandStack to create children node of the new operator node
            }
            operatorStack.push(Character.toString(currentChar)); // Push the operator into the operator list
        }
    }

    // Handle opening parenthesis analysis and create a subtree
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

    // Handle closing parenthesis analysis, update the subtree until the previous operator is a '('
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
        return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
    }

    // Determine operator precedence
    private int precedence(String operator)
    {
        switch (operator) {
            // Addition and subtraction are 1 because multiplication and division have priority over them, those are 2
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    // Method to get the previous non-whitespace character from the expression
    private char getPreviousChar(String expression, int i)
    {
        if (i <= 0)
        {
            return '\0'; // Return '\0' if there is no previous character
        }

        int j = i - 1;
        // Search backwards for the previous non-whitespace character
        while (j >= 0 && Character.isWhitespace(expression.charAt(j)))
        {
            j--;
        }

        // Return the previous non-whitespace character
        return expression.charAt(j);
    }

    // Parse the expression into an expression tree
    public CalculatorNodes parseExpression(String expression)
    {
        int length = expression.length();
        int openParenthesesCount = 0;  // Counter for unmatched opening parentheses

        for (int i = 0; i < length; i++) {
            // The current character of the string provided by the user
            char currentChar = expression.charAt(i);
            // The previous character of the string provided by the user that isn't a ' '
            char previousChar = getPreviousChar(expression, i);

            try
            {
                // If the current character is a digit or a decimal point, call the corresponding method
                if (Character.isDigit(currentChar) || currentChar == '.')
                {
                    analyzeDecimalNumber(currentChar);
                }
                // If the current character is an operator, call the corresponding method
                else if (isOperator(currentChar))
                {
                    analyzeOperator(currentChar, previousChar);
                }
                // If the current character is an opening parenthesis, call the corresponding method
                else if (currentChar == '(')
                {
                    analyzeOpeningParenthesis(previousChar);
                    openParenthesesCount++;
                }
                // If the current character is a closing parenthesis, call the corresponding method
                else if (currentChar == ')')
                {
                    analyzeClosingParenthesis();
                    openParenthesesCount--;
                    if (openParenthesesCount < 0)
                    {
                        throw new IllegalArgumentException("Error: Extra closing parenthesis.");
                    }
                }
                // If the current character is not a whitespace and not a valid character, throw an exception
                else if (!Character.isWhitespace(currentChar))
                {
                    throw new IllegalArgumentException("Error: Invalid character '" + currentChar + "'.");
                }
            }
            catch (EmptyStackException e)
            {
                // Catch any EmptyStackException which indicates an invalid expression due to mismatched operators or operands
                throw new IllegalArgumentException("Error: Invalid expression due to mismatched operators or operands.");
            }
        }

        // Handle the last number in the string by converting it through the numberBuilder
        updateNumberBuilder();

        // Check if there are any unmatched opening parentheses and throw an error if so
        if (openParenthesesCount > 0)
        {
            throw new IllegalArgumentException("Error: Missing closing parenthesis.");
        }

        // Process remaining operators in the stack through the updateStacks method that creates nodes
        while (!operatorStack.isEmpty())
        {
            updateStacks();
        }

        // Check if there is exactly one operand left in the stack, otherwise the expression is invalid
        if (operandStack.size() != 1)
        {
            throw new IllegalArgumentException("Error: Invalid expression. Operand stack size is " + operandStack.size() + ".");
        }

        // The final tree is in the operand stack, return its value to have result
        return operandStack.pop();
    }
}