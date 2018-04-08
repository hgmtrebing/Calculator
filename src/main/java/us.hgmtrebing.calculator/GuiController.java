package us.hgmtrebing.calculator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//TODO - Validate order of characters being added
//TODO - figure out best method to update expressionDisplay and resultDisplay
public class GuiController implements Initializable{

    private final String validationString;
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

        ansKey.setFocusTraversable(false);

        backspaceKey.setOnAction(event -> {
            this.removeCharacterFromDisplay();
        });
        backspaceKey.setFocusTraversable(false);

        //Adds an event handler for keyboard events
        //This allows the user to type, rather than press buttons
        gridPane.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            KeyCode code = event.getCode();
            if (code.equals(KeyCode.BACK_SPACE)) {
                removeCharacterFromDisplay();
            } else if (character.matches(this.validationString)) {
                addTextToDisplay(character);
            } else if (character.equals(".")) {
                //TODO - think of a better way to add decimal points
                //right now, the user can add as many as (s)he wants
                //addTextToDisplay(character);
            }
        });

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
        //If expression was just evaluated, "blank out" the expression display and add the new character
        if (this.evaluated) {
            this.expressionDisplay.setText("");
            this.evaluated = false;
        }
        this.expressionDisplay.setText( this.expressionDisplay.getText() + text);
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
        String expression = this.expressionDisplay.getText();
        String result = this.calculator.evaluate(expression).toString();

        this.resultDisplay.setText( result );
        this.evaluated = true;
    }
}
