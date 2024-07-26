// Class that holds the mathematical operation logic
public class CalculatorMathLogic
{
    /** Constants */
    protected static final char ADDITION = '+';
    protected static final char SUBTRACTION = '-';
    protected static final char MULTIPLICATION = '*';
    protected static final char DIVISION = '/';
    protected static final char RESET = '.';

    /** Fields */
    // Subtotal is the current number
    protected double subtotal;

    /** Methods */
    // Constructor of CalculatorMathLogic
    public CalculatorMathLogic()
    {
        // Initialize subtotal to 0
        subtotal = 0;
    }

    // Method to choose the next mathematical operation
    public void chooseOperation(char choice, double value)
    {
        // Determine the operation based on user's choice
        switch (choice)
        {
            case ADDITION:
                add(value);
                break;
            case SUBTRACTION:
                subtract(value);
                break;
            case MULTIPLICATION:
                multiply(value);
                break;
            case DIVISION:
                divide(value);
                break;
            case RESET:
                // Reset subtotal to 0
                subtotal = 0;
                break;
        }
    }

    // Method that returns the current subtotal
    public double getSubtotal()
    {
        return subtotal;
    }

    // Method that changes the current subtotal
    public void setSubtotal(double value)
    {
        subtotal = value;
    }

    // Method that adds another value to the subtotal
    public void add(double value)
    {
        subtotal += value;
    }

    // Method that subtracts another value from the subtotal
    public void subtract(double value)
    {
        subtotal -= value;
    }

    // Method that multiplies another value to the subtotal
    public void multiply(double value)
    {
        subtotal *= value;
    }

    // Method that divides the subtotal by another value
    public void divide(double value)
    {
        if (value != 0)
        {
            subtotal /= value;
        }
        else
        {
            System.out.println("Error: Division by zero.");
        }
    }
}
