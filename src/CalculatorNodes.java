// Class that holds the mathematical operation logic
public class CalculatorNodes {

    /** Fields */
    // Contains two children emplacement, and its own value that can be an operand or an operator
    CalculatorNodes left, right;
    String value;

    /** Methods */
    // Constructor for a leaf node
    public CalculatorNodes(String value)
    {
        this.value = value; // Updating the value by the argument
        left = right = null; // Setting children nodes as null
    }

    // Constructor for a node with children (operator node)
    public CalculatorNodes(String value, CalculatorNodes left, CalculatorNodes right)
    {
        this.value = value; // Updating the value by the argument
        this.left = left; // Updating the left child to another node
        this.right = right; // Updating the right child to another node
    }

    // Method to evaluate the expression tree
    public double evaluate()
    {
        // If the node is a leaf node, return its value
        if (left == null && right == null) {
            return Double.parseDouble(value);
        }

        // if the node is an operator, evaluate his children recursively
        double leftValue = left.evaluate();
        double rightValue = right.evaluate();

        // Switch value to do the according mathematical operation and return the result
        switch (value) {
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "*":
                return leftValue * rightValue;
            case "/":
                if (leftValue != 0) {
                    return leftValue / rightValue;
                } else {
                    throw new ArithmeticException("Division by zero.");
                }
            default:
                throw new IllegalArgumentException("Unknown operator: " + value);
        }
    }
}
