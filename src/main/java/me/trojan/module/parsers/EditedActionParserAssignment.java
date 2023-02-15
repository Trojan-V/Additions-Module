package me.trojan.module.parsers;

import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.util.regex.Matcher;

public class EditedActionParserAssignment extends ActionParserAbstract {
    private String actionName;
    private String varName;
    private String expression;
    private int equalsCharIndex;
    private String scriptEntry;

    private int additionCharIndex;
    private int subtractionCharIndex;
    private int multiplicationCharIndex;
    private int divisionCharIndex;
    private int moduloCharIndex;
    private int setCharIndex;

    public EditedActionParserAssignment(ScriptContext context) {
        super(context);
    }

    @Override
    public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
        /*  Set the scriptEntry passed into this method to the scriptEntry variable defined as class attribute
         to be able to use it throughout the entire class (increase scope). */
        this.scriptEntry = scriptEntry;

        /* The decimal 61 from the ASCII char table represents the '=' (equals) character.
         IndexOf searches for the first occurrence of the '=' char. */
        equalsCharIndex = getScriptEntry().indexOf(61);

        if (getEqualsCharIndex() == -1)
            return null;

        expression = scriptEntry.substring(getEqualsCharIndex() + 1).trim();
        /* Default actionName and varName if none of the operationModifiers (+=, -=, *=, :=, /=) have been assigned. */
        actionName = "EDITEDASSIGN";

        additionCharIndex = getScriptEntry().indexOf(43);
        subtractionCharIndex = getScriptEntry().indexOf(45);
        multiplicationCharIndex = getScriptEntry().indexOf(42);
        divisionCharIndex = getScriptEntry().indexOf(47);
        moduloCharIndex = getScriptEntry().indexOf(37);
        setCharIndex = getScriptEntry().indexOf(58);

        /* Check for other possible Expression modifications, such as compound operators or := (SET). */
        checkForEqualsCharacterModifiersAndAssignValuesToClassAttributes();

        /* Validate the variableName, or exit the parse-method. */
        if (!Variable.isValidVariableOrArraySpecifier(getVarName()))
            return null;

        /* Using Matcher#matches() instead of Matcher#find() here to ensure the entire expression, and not only parts of it are matching the pattern.
            As further explanation: Matcher#matches() adds a '^' in the beginning of the RegEx and a '$' at the end of the RegEx.
            So, if Matcher#find() is being used with a '^' and a '$', it's functionality is (from what I know) the same as Matcher#matches().
            For more information: https://stackoverflow.com/questions/4450045/difference-between-matches-and-find-in-java-regex
            Example Match for PATTERN_SCRIPTACTION: boolean = isRunning(<param>);

            The content of the groups which should be there if the expression matches the PATTERN_SCRIPTACTION.
            Example: slotclick(10,left,false)
            matcher#group(1) will be 'slotclick'
            matcher#group(2) will be '10,left,false' */
        Matcher actionMatcher = ActionParser.PATTERN_SCRIPTACTION.matcher(expression);
        if (actionMatcher.matches()) {
            return this.parse(actionProcessor, actionMatcher.group(1), actionMatcher.group(2), varName);
        }

        if (isExpressionString()) {
            StringBuilder rawParams = new StringBuilder();
            ScriptCore.tokenize(getExpression(), ' ', '"', '"', '\\', rawParams);
            expression = rawParams.length() > 0 ? rawParams.substring(1) : "";
        }

        /* Example: #int = #another_int +17;
         Params string which will be passed as parameter into the ScriptAction which is being called, in this case "MODULO|SET|INC|DEC|ASSIGN".
         --------------------------------------------------------------------------------------------------------------------------------------
         The first parameter is the variableName, in the example used here it would be '#int'.
         The second parameter is the expression, in the example used here it would be '#another_int +17'.
         These parameters are then available through the String[] params in the public IReturnValue execute() */
        String[] params = {getVarName(), getExpression()};
        return this.getInstance(actionProcessor, getActionName(), getExpression(), getExpression(), params, null);
    }

    /* This method checks if there is any other character in front of the '=' character
     and assigns values to variables depending on which if-statement matches.
     In order to properly support compound assignment operators as well as the set operator :=
     this method has to be invoked.
      @See https://www.torsten-horn.de/techdocs/ascii.htm */
    private void checkForEqualsCharacterModifiersAndAssignValuesToClassAttributes() {
        if(isAdditionCompoundOperator()) {
            actionName = "INC";
            varName = getScriptEntry().substring(0, additionCharIndex);
        }
        else if(isSubtractionCompoundOperator()) {
            actionName = "DEC";
            varName = getScriptEntry().substring(0, subtractionCharIndex);
        }
        else if(isMultiplicationCompoundOperator()) {
            actionName = "MULTIPLY";
            varName = getScriptEntry().substring(0, multiplicationCharIndex);
        }
        else if(isDivisionCompoundOperator()) {
            actionName = "DIVIDE";
            varName = getScriptEntry().substring(0, divisionCharIndex);
        }
        else if(isModuloOperator()) {
            actionName = "MODULO";
            varName = getScriptEntry().substring(0, moduloCharIndex);
        }
        else if(isSetOperator()) {
            actionName = "SET";
            varName = getScriptEntry().substring(0, setCharIndex);
        }
        else {
            varName = getScriptEntry().substring(0, getEqualsCharIndex());
        }
        varName = varName.trim();
    }

    /**
     * @return true if the expression begins and ends with a quotation mark.
     */
    private boolean isExpressionString() {
        return getExpression().trim().startsWith("\"") && getExpression().trim().endsWith("\"");
    }

    /**
     * @return true if a '+' character is in front of the '=' character.
     */
    private boolean isAdditionCompoundOperator() {
        return additionCharIndex == getEqualsCharIndex() - 1;
    }

    /**
     * @return true if a '-' character is in front of the '=' character.
     */
    private boolean isSubtractionCompoundOperator() {
        return subtractionCharIndex == getEqualsCharIndex() - 1;
    }

    /**
     * @return true if a '*' character is in front of the '=' character.
     */
    private boolean isMultiplicationCompoundOperator() {
        return multiplicationCharIndex == getEqualsCharIndex() - 1;
    }

    /**
     * @return true if a '/' character is in front of the '=' character.
     */
    private boolean isDivisionCompoundOperator() {
        return divisionCharIndex == getEqualsCharIndex() - 1;
    }

    private boolean isModuloOperator() {
        return moduloCharIndex == getEqualsCharIndex() - 1;
    }

    /**
     * The decimal 58 from the ASCII char table represents the ':' (colon) character.
     * IndexOf searches for the first occurrence of the ':' char.
     *
     * @return true if a ':' character is in front of the '=' character.
     */
    private boolean isSetOperator() {
        return setCharIndex == getEqualsCharIndex() - 1;
    }

    /**
     * @return Represents the "right side of the '=' character.
     * Example:
     * --------------------------------------------------------------------------------------------------
     * #int = #another_int +17;
     * #another_int +17; is the part of the expression which is being stored in this String (expression).
     */
    private String getExpression() {
        return expression;
    }

    /**
     * @return the index of the '=' character within the scriptEntry.
     * Used to split the scriptEntry into the varName (left side) and the expression (right side).
     */
    private int getEqualsCharIndex() {
        return equalsCharIndex;
    }

    /**
     * @return the entire scriptEntry.
     * Example: #int = #another_int + 17;
     * This would return '#int = #another_int + 17'
     */
    private String getScriptEntry() {
        return scriptEntry;
    }

    /**
     * @return The name of the ScriptAction which should be called by this ActionParser, can be found as parameter of the constructor for ScriptActions.
     */
    private String getActionName() {
        return actionName;
    }

    /**
     * @return The variable name (left side of the scriptEntry).
     * The variable on the left side of the expression (String varName). The result of the expression will be stored in this variable.
     * Example:
     * --------------------------------------------------------------------------------------------------------------
     * #int = #another_int + 17;
     * #int is the part of the expression which is being stored in this String (varName).
     */
    private String getVarName() {
        return varName;
    }
}
