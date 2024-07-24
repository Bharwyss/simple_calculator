import java.util.Scanner;

// Class that holds the input/output logic, and displays the data
public class Calculator
{
    /** Constants */
    protected static final int START = 0;
    protected static final int ADD_INPUT1 = 1;
    protected static final int ADD_INPUT2 = 2;
    protected static final int ADD_SYMBOL = 3;
    protected static final int RESULT = 4;

    /** Fields */
    // Scanner that reads user input
    protected static Scanner scanner = new Scanner(System.in);
    // Current mathematical state of the calculator
    protected static CalculatorMathLogic mathLogic;
    // Current state of calculator
    protected static int state = START;
    // Choice of symbol from user
    protected static int choice;
    // Choice of second value from user
    protected static double value;

    /** Methods */
    public static void main(String[] args)
    {
        // Initialize the math logic
        mathLogic = new CalculatorMathLogic();
        // Start the calculator process
        setCalculator();
    }

    // Method to handle user input for numbers
    public static double getNumberUser()
    {
        while (true)
        {
            System.out.println("Choose a number between 0 and 9:");
            if (scanner.hasNextDouble())
            {
                double value = scanner.nextDouble();
                if (value < 10 && value >= 0)
                {
                    return value;
                }
                else
                {
                    System.out.println("Wrong input. Please enter a number between 0 and 9.");
                }
            }
            else
            {
                System.out.println("Wrong input. Please enter a valid number.");
                scanner.next();
            }
        }
    }

    // Method that asks what kind of operation the user wants
    public static int getChoiceUser(double value)
    {
        while (true)
        {
            System.out.println("Choose an operator: +, -, x, /; Press . to reset");
            switch (scanner.next().charAt(0))
            {
                case '+':
                    System.out.println("Current subtotal = " + mathLogic.getSubtotal() + " + " + value);
                    return 0;
                case '-':
                    System.out.println("Current subtotal = " + mathLogic.getSubtotal() + " - " + value);
                    return 1;
                case 'x':
                    System.out.println("Current subtotal = " + mathLogic.getSubtotal() + " x " + value);
                    return 2;
                case '/':
                    System.out.println("Current subtotal = " + mathLogic.getSubtotal() + " / " + value);
                    return 3;
                case '.':
                    return 4;
                default:
                    System.out.println("Invalid input. Please enter a valid operator.");
            }
        }
    }

    // Method that returns true or false based on user input for yes/no questions
    public static boolean askYesOrNo(String message)
    {
        while (true)
        {
            System.out.println(message);
            String input = scanner.next().toLowerCase();
            if (input.equals("yes"))
            {
                return true;
            }
            else if (input.equals("no"))
            {
                return false;
            }
            else
            {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    // Method to start the Calculator
    public static void setStart()
    {
        // Reset subtotal to 0
        mathLogic.setSubtotal(0);
        // Display the current subtotal
        System.out.println("Current subtotal = " + mathLogic.getSubtotal());
        // Move to the next state
        state = ADD_INPUT1;
    }

    // Method to add input1 from user
    public static void setAddInput1()
    {
        // Get the first number input from user
        mathLogic.setSubtotal(getNumberUser());
        // Move to the next state
        state = ADD_INPUT2;
    }

    // Method to add input2 from user
    public static void setAddInput2()
    {
        // Get the second number input from user
        value = getNumberUser();
        // Move to the next state
        state = ADD_SYMBOL;
    }

    // Method to add symbol from user
    public static void setAddSymbol()
    {
        // Get the operator input from user
        choice = getChoiceUser(value);
        // If user chooses reset, go to the START state
        if (choice == 4)
        {
            state = START;
        }
        else
        {
            // Otherwise, go to the RESULT state
            state = RESULT;
        }
    }

    // Method to show result and ask for more operations
    public static void setResult()
    {
        // Perform the chosen operation
        mathLogic.chooseOperation(choice, value);
        // Display the result of the operation
        System.out.println("This operation equals = " + mathLogic.getSubtotal());
        // Ask if the user wants to continue
        boolean continueOperation = askYesOrNo("Continue operation?");
        if (continueOperation)
        {
            // If yes, go back to ADD_INPUT2 state for another operation
            state = ADD_INPUT2;
        }
        else
        {
            // Otherwise, reset and go to the START state
            mathLogic.chooseOperation(CalculatorMathLogic.RESET, 0);
            state = START;
        }
    }

    // Method to run the calculator
    public static void setCalculator()
    {
        // Loop through the states of the calculator
        while (true)
        {
            switch (state)
            {
                case START:
                    setStart();
                    break;
                case ADD_INPUT1:
                    setAddInput1();
                    break;
                case ADD_INPUT2:
                    setAddInput2();
                    break;
                case ADD_SYMBOL:
                    setAddSymbol();
                    break;
                case RESULT:
                    setResult();
                    break;
            }
        }
    }
}