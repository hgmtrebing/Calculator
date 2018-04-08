package us.hgmtrebing.calculator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//TODO - Validate order of characters being added
//TODO - figure out best method to update expressionDisplay and resultDisplay
public class GuiController implements Initializable{

    private final String validationString;
    private final Pattern decimalValidationPattern; //special validation String is needed for decimals
    private final Pattern characterValidationPattern;
    private final Calculator calculator = new Calculator ();
    private boolean evaluated = false;

    @FXML private Button num1;
    @FXML private Button num2;
    @FXML private Button num3;
    @FXML private Button num4;
    @FXML private Button num5;
    @FXML private Button num6;
    @FXML private Button num7;
    @FXML private Button num8;
    @FXML private Button num9;
    @FXML private Button num0;
    @FXML private Button ansKey;
    @FXML private Button clrKey;
    @FXML private Button plusKey;
    @FXML private Button minusKey;
    @FXML private Button timesKey;
    @FXML private Button divideKey;
    @FXML private Button equalsKey;
    @FXML private Button lparenKey;
    @FXML private Button rparenKey;
    @FXML private Button backspaceKey;
    @FXML private Label expressionDisplay;
    @FXML private Label resultDisplay;
    @FXML private GridPane gridPane;

    //Instance Initializer
    //Used here to create String to be assigned to validationString
    {
        StringBuilder sb = new StringBuilder();
        for (TokenType t : TokenType.values()) {
            //Taken from Tokenizer - more comments available there
            sb.append(String.format("|(?<%s>%s)", t.name(), "\\G" + t.getSymbols()));
        }
        this.validationString = sb.toString();
        /* validationString is a Pattern that validates the input of all non-decimal characters
         * validationString is formed using regex information from TokenType
         * It is the first method used to validate characters inputted from the keyboard
         */

        this.characterValidationPattern = Pattern.compile("\\d\\D?-?\\z");
        /* operatorValidationPattern is a special pattern to validate the addition of new operators
         * Operators must be preceded by a digit, and may be followed by a negative sign
         */

        this.decimalValidationPattern = Pattern.compile("(^|[\\D&&[^\\.]])\\d+\\.\\z");
        /* decimalValidationPattern is a special Pattern that protects against the insertion of erroneous decimal points
         * Erroneous decimal points include:
         *  1) Decimals that are inserted back-to back, eg: 45...931
         *  2) Decimals that are inserted when a number already contains a decimal, eg: 49.41523.42
         *  3) Decimals that are inserted outside of a number, eg: 72+.-45
         *
         * NOTE: This Pattern does not protect against a trailing decimal point at the end of the number, eg.
         * 45111. or 45121.*45
         * Other areas of the application must protect against them
         *
         * A brief synopsis of how this regular expression works:
         *  1) If the user enters a decimal, it will be right before the end of input (\\.\\z)
         *  2) Directly before the decimal, there should be 1 or more numeric digits (\\d+)
         *  3)At the front of the string of digits should be:
         *      3a. The beginning of the line (^)
         *      3b. A non-digit character that IS NOT a decimal ([\\D&&[^\\.]])
         */
    }
    @Override
    public void initialize (URL location, ResourceBundle bundle) {

        //Set event handlers for buttons
        num0.setOnAction(event ->{
            addTextToDisplay("0");
        });
        num0.setFocusTraversable(false);

        num1.setOnAction(event -> {
            addTextToDisplay("1");
        });
        num1.setFocusTraversable(false);

        num2.setOnAction(event -> {
            addTextToDisplay("2");
        });
        num2.setFocusTraversable(false);

        num3.setOnAction(event -> {
            addTextToDisplay("3");
        });
        num3.setFocusTraversable(false);

        num4.setOnAction(event -> {
            addTextToDisplay("4");
        });
        num4.setFocusTraversable(false);

        num5.setOnAction(event -> {
            addTextToDisplay("5");
        });
        num5.setFocusTraversable(false);

        num6.setOnAction(event -> {
            addTextToDisplay("6");
        });
        num6.setFocusTraversable(false);

        num7.setOnAction(event -> {
            addTextToDisplay("7");
        });
        num7.setFocusTraversable(false);

        num8.setOnAction(event -> {
            addTextToDisplay("8");
        });
        num8.setFocusTraversable(false);

        num9.setOnAction(event -> {
            addTextToDisplay("9");
        });
        num9.setFocusTraversable(false);

        plusKey.setOnAction(event -> {
            addTextToDisplay("+");
        });
        plusKey.setFocusTraversable(false);

        minusKey.setOnAction(event -> {
            addTextToDisplay("-");
        });
        minusKey.setFocusTraversable(false);

        timesKey.setOnAction(event -> {
            addTextToDisplay("*");
        });
        timesKey.setFocusTraversable(false);

        divideKey.setOnAction(event -> {
            addTextToDisplay("/");
        });
        divideKey.setFocusTraversable(false);

        lparenKey.setOnAction(event -> {
            addTextToDisplay("(");
        });
        lparenKey.setFocusTraversable(false);

        rparenKey.setOnAction(event -> {
            addTextToDisplay(")");
        });
        rparenKey.setFocusTraversable(false);

        equalsKey.setOnAction(event -> {
            this.evaluate();
        });
        equalsKey.setFocusTraversable(false);

        clrKey.setOnAction(event -> {
            this.expressionDisplay.setText("");
            this.resultDisplay.setText("");
        });
        clrKey.setFocusTraversable(false);

        ansKey.setOnAction(event -> {
            this.addTextToDisplay( this.resultDisplay.getText() );
        });
        ansKey.setFocusTraversable(false);

        backspaceKey.setOnAction(event -> {
            this.removeCharacterFromDisplay();
        });
        backspaceKey.setFocusTraversable(false);

        //Adds an event handler for keyboard events
        //This allows the user to type, rather than press buttons
        gridPane.setOnKeyTyped(event -> {
            addTextToDisplay( event.getCharacter() );
        });

        //Sets event handlers for backspace and enter keys
        //For some reason, JavaFX can only work with these if used with setOnKeyPressed(), not setOnKeyTyped() [used above]
        gridPane.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code.equals(KeyCode.BACK_SPACE)) {
                removeCharacterFromDisplay();
            } else if (code.equals(KeyCode.ENTER)) {
                evaluate();
            }
        });
    }

    private void addTextToDisplay(String text) {
        String possibleNewExpression = this.expressionDisplay.getText() + text;

        if ( text.matches(this.validationString) && characterValidationPattern.matcher(possibleNewExpression).find()) {

            //If expression was just evaluated, "blank out" the expression display and add the new character
            if (this.evaluated) {
                this.expressionDisplay.setText("");
                this.evaluated = false;
            }
            this.expressionDisplay.setText(possibleNewExpression);

        } else if (text.equals(".") && decimalValidationPattern.matcher(possibleNewExpression).find()) {

            //If expression was just evaluated, "blank out" the expression display and add the new character
            if (this.evaluated) {
                this.expressionDisplay.setText("");
                this.evaluated = false;
            }
            this.expressionDisplay.setText(possibleNewExpression);

        }
    }

    private void removeCharacterFromDisplay() {
        String expression = this.expressionDisplay.getText();
        //If expression has 1 or more characters, remove the final character (basically, a backspace operation)
        if (expression.length() > 0) {
            expression = expression.substring(0, expression.length()-1);
        }
        this.expressionDisplay.setText(expression);
    }

    private void evaluate () {
        /*Try-catch clause necessary because it is possible for the user to try and evaluate illegal expression
         *Because of previous validations which sanitize input, the only illegal expressions are ones that possible are incomplete expressions
         *Aka, expressions that are legal up until the end of input, for example:
         *  a. 61+45112.
         *  b. 59*
         *  c. 41.3*-
         */
        try {
            String result = this.expressionDisplay.getText();
            result = this.calculator.evaluate(result).toString();
            this.resultDisplay.setText( result );
            this.evaluated = true;
        } catch (Exception e) {}
        //catch clause does nothing because, by this point, illegal expressions are merely incomplete
        //After the illegal expression is caught, the user should be allowed to complete the expression and re-evaluated it
    }
}
