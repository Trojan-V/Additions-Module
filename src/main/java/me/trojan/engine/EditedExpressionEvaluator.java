package me.trojan.engine;

import com.mumfrey.liteloader.util.log.LiteLoaderLogger;
import net.eq2online.console.Log;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.IExpressionEvaluator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EditedExpressionEvaluator implements IExpressionEvaluator {
    public static boolean TRACE = false;
    private static final int MAX_DEPTH = 10;
    private static final Pattern PATTERN_OPERATOR = Pattern.compile("={1,2}|<=|>=|>|<|!=|&{2}|\\|{1,2}|\\+|-|\\*|/|%|\\^", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_STRING = Pattern.compile("\\x22([^\\x22]*)\\x22");
    private static final Pattern PATTERN_NEGATIVE_NUMBER = Pattern.compile("(?<=(^|\\())-(?=[0-9])");
    private final HashMap<String, Integer> variables = new HashMap<>();
    private final HashMap<String, Integer> stringLiterals = new HashMap<>();
    private final HashMap<Integer, String> stringLiteralValues = new HashMap<>();
    private final String originalExpression;
    private final IScriptActionProvider provider;
    private final IMacro macro;
    private int nextStringLiteral = 2147483646;
    private int result;

    public EditedExpressionEvaluator(String expression, IScriptActionProvider provider, IMacro macro) {
        this.originalExpression = expression;
        this.provider = provider;
        this.macro = macro;
    }

    /* Set the content of a variable to a boolean value ( 1 or 0) */
    public void setVariable(String variableName, boolean variableValue) {
        variables.put(variableName, variableValue ? 1 : 0);
    }

    /* Set the content of a variable to an integer value */
    public void setVariable(String variableName, int variableValue) {
        variables.put(variableName, variableValue);
    }

    /* Set the content of a variable to a String value */
    public void setVariable(String variableName, String variableValue) {
        variables.put(variableName, addStringLiteral(variableValue));
    }

    /* Set the value of all variables in the variables Map, checking if the value is a Boolean, a String or an Integer. */
    public void setVariables(Map<String, Object> variables) {
        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            Object variableValue = variable.getValue();
            String variableKey = variable.getKey();
            if (variableValue instanceof Boolean)
                setVariable(variableKey, (Boolean) variableValue);
            else if (variableValue instanceof String)
                setVariable(variableKey, (String) variableValue);
            else if (variableValue instanceof Integer)
                setVariable(variableKey, (Integer) variableValue);
        }
    }

    /* Write all variables which are currently stored in the variables Map to the liteloader.log file */
    public void dumpVariables() {
        for (Map.Entry<String, Integer> variable : variables.entrySet()) {
            Object variableValue = variable.getValue();
            String variableKey = variable.getKey();
            if (stringLiteralValues.containsKey(variableValue))
                Log.info("dumpVariables() {0}={1}", variableKey, stringLiteralValues.get(variableValue));
            else
                Log.info("dumpVariables() {0}={1}", variableKey, variableValue);
        }
    }

    /* During my testings, this method hasn't been called a single time. */
    public int addStringLiteral(String literalString) {
        if (literalString.length() == 0)
            return 0;

        String cleanedLiteralString = literalString.replaceAll("\\x20\\|&!><=", "\u0080");
        if (stringLiterals.containsKey(cleanedLiteralString))
            return stringLiterals.get(cleanedLiteralString);

        int literalStringIndex = nextStringLiteral--;
        stringLiterals.put(cleanedLiteralString, literalStringIndex);
        stringLiteralValues.put(literalStringIndex, literalString);
        return literalStringIndex;
    }

    public int evaluate() {
        try {
            String expression = prepareExpressionForEvaluation();
            result = this.evaluate(expression, 0);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    public int getResult() {
        return result;
    }

    /* Removes certain parts of the expression, or replaces them.
     for example, all whitespaces are replaced with "" or all "true" values are replaced with 1 and all "false" values with 0. */
    private String prepareExpressionForEvaluation() {
        String expression = originalExpression;
        Matcher stringLiteralPatternMatcher = PATTERN_STRING.matcher(expression);

        while(stringLiteralPatternMatcher.find()) {
            int stringLiteralIndex = addStringLiteral(stringLiteralPatternMatcher.group(1));
            expression = expression.substring(0, stringLiteralPatternMatcher.start()) + stringLiteralIndex + expression.substring(stringLiteralPatternMatcher.end());
            stringLiteralPatternMatcher.reset(expression);
        }

        expression = expression.replaceAll("(?<!&)& ", "\\&\\&")
                .replaceAll("\\s", "")
                .replaceAll("[Tt][Rr][Uu][Ee]", "1")
                .replaceAll("[Ff][Aa][Ll][Ss][Ee]", "0");
        /* After these replaceAll calls, expressions look like: #i+5 instead of #i + 5 */
        if (TRACE)
            Log.info("[LOG]   Prepared [{0}]", expression);
        return expression;
    }

    private int evaluate(String expression, int depth) {
        //Game.addChatMessage("Expression: " + expression + "; Depth: " + depth);
        if (expression == null || expression.length() < 1 || depth >= MAX_DEPTH)
            return 0;
        if (TRACE)
            Log.info("[LOG]    Evaluating [{0}]", expression);

        Matcher negativeNumberPatternMatcher = PATTERN_NEGATIVE_NUMBER.matcher(expression);
        expression = negativeNumberPatternMatcher.replaceAll("¬");
        if (TRACE)
            Log.info("[LOG]     Evaluating [{0}]", expression);

        /* If the expression contains an operator, such as %, *, /, +, -, =, ==, >= etc...
         call other evaluate method to properly calculate the subExpressionresult. */
        if (containsNoParentheses(expression)) {
            if (containsOperator(expression)) {
                Matcher operatorMatcher = PATTERN_OPERATOR.matcher(expression);
                operatorMatcher.find();
                /* Expression: 7 + 3 -> will actually be parsed as 7+3 (no whitespaces)
                 So the Matcher#start and Matcher#end methods will always provide the left side of the operator and the right side */
                String sLHS = expression.substring(0, operatorMatcher.start());
                String sRHS = expression.substring(operatorMatcher.end());
                int expressionResult = this.evaluate(sLHS, sRHS, operatorMatcher.group(), depth);
                if (TRACE)
                    Log.info("[LOG]       Calculated [{0}]", expressionResult);
                return expressionResult;
            }
            return this.evaluateSingle(expression, depth + 1);
        }


        /* indexOf(40) -> '(' */
        int startingOpeningBracketIndex = expression.indexOf(40);
        int endPos = startingOpeningBracketIndex + 1;

        /* Increase stackCount each time an opening bracket is found and decrement stackCount each time a closing bracket is found. */
        for(int stackCount = 0; endPos < expression.length(); endPos++) {
            if (expression.charAt(endPos) == '(')
                stackCount++;

            if (expression.charAt(endPos) == ')') {
                stackCount--;
                if (stackCount < 0)
                    break;
            }
        }

        String subExpression = expression.substring(startingOpeningBracketIndex + 1, endPos);
        /* Recursively call evaluate with the newly created subExpression and increase the depth variable, which will ensure that brackets are evaluated first. */
        int subExpressionResult = this.evaluate(subExpression, depth + 1);

        /* Expression: (7 + 3) / 5 -> leftOverFromEntireExpression will be /5 */
        String leftOverFromEntireExpression = endPos < expression.length() ? expression.substring(endPos + 1) : "";

        /* For clarification purposes, check this imgur link. -> https://imgur.com/a/qqi8NMQ */
        if (startingOpeningBracketIndex > 0) {
            /* Expression: (7+3)*(7+2) -> First of all, the first bracket will be resolved which leads to the following next expression:
             * 10*(7+2) -> Now, the startingOpeningBracketIndex is > 0 and this if-branch is being called.
             * This first part (VARIABLE_NAME) is just being taken over as it is (as it's already evaluated down to a single value), the part within the brackets is being resolved.
             * expression.substring(0, startingOpeningBracketIndex) -> 10*(
             * So the new expression looks like this: 10*9 (as the subExpression has been handled above and the result has been stored in the subExpressionResult integer.
             */
            expression = expression.substring(0, startingOpeningBracketIndex) + subExpressionResult + leftOverFromEntireExpression;
        } else {
            /* Expression: (10/5) -> This else branch will be called if the expression looks like this,
             * and we can process this expression without having to care about any preceding characters.
             * All subExpressions (expressions which contain parenthesis) are being processed first, then the new expression string is being assembled
             * and then passed into this method again by using recursion.
             * This recursive behaviour is going on until all subexpressions and then the final expression have been processed. */
            expression = "" + subExpressionResult + leftOverFromEntireExpression;
        }
        return this.evaluate(expression, depth);
    }


    /**
     * @code Example -> 7 * 3
     * @param sLHS Left side of the expression, here 7
     * @param sRHS Right side of the expression, here 3
     * @param operator Operator between sLHS and sRHS, here *
     * @param depth The level of parenthesis: 7 * 3 would be depth 0, (7 * 3) would be depth 1, ((7 * 3)) would be depth 2, and so on...
     * @return The result of the expression, here this would be 21 (because 7 * 3 = 21)
     */
    private int evaluate(String sLHS, String sRHS, String operator, int depth) {
        //Game.addChatMessage("Called evaluate method from EditedExpressionEvaluator.");
        //Game.addChatMessage("LHS: " + sLHS);
        //Game.addChatMessage("RHS: " + sRHS);
        //Game.addChatMessage("Operator: " + operator);
        //Game.addChatMessage("Depth: " + depth);
        int lhs = getValue(sLHS, depth);
        int rhs = getValue(sRHS, depth);
        if (TRACE)
            Log.info("[LOG]      Calculating [{0}] with {1} {2} at depth {3} values [{4}] [{5}]", operator, sLHS, sRHS, depth, lhs, rhs);

        switch (operator) {
            case "+":
                return lhs + rhs;
            case "-":
                return lhs - rhs;
            case "*":
                return lhs * rhs;
            case "/":
                return lhs / rhs;
            case "%":
                return lhs % rhs;
            case "^":
                return (int) Math.pow(lhs, rhs);
        }

        /* If none of the operators checked within the switch statement are matching, call evaluateBoolean. */
        return this.evaluateBoolean(lhs, rhs, operator) ? 1 : 0;
    }

    private boolean evaluateBoolean(int lhs, int rhs, String operator) {
        switch (operator) {
            case "=":
            case "==":
                return lhs == rhs;
            case "!=":
                return lhs != rhs;
            case "<=":
                return lhs <= rhs;
            case ">=":
                return lhs >= rhs;
            case "<":
                return lhs < rhs;
            case ">":
                return lhs > rhs;
            case "&":
            case "&&":
                return lhs > 0 && lhs < nextStringLiteral && rhs > 0 && rhs < nextStringLiteral;
            case "|":
                return lhs > 0 && lhs < nextStringLiteral || rhs > 0 && rhs < nextStringLiteral;
        }
        if (!"||".equals(operator))
            return false;

        return lhs > 0 && lhs < nextStringLiteral || rhs > 0 && rhs < nextStringLiteral;
    }

    /**
     * This method is solely responsible for validating that an integer is not longer than the integer limit (nextStringLiteral) and will return 1 if it's longer or equal.
     * Executed whenever an expression does not require any mathematical operations, i.e. #int = 7 would be a case where this method gets called.
     * @param single The number which should be assigned, in this example 7
     * @param depth Increases with each level of parenthesis added (default: 1)
     * @return The value after validating that it isn't longer than nextStringLiteral, otherwise returns 1.
     */
    private int evaluateSingle(String single, int depth) {
        int value = getValue(single, depth);
        return value < nextStringLiteral ? value : 1;
    }


    private int getValue(String expression, int depth) {
        boolean not = false;
        /* Check if the expression has a leading '!', if yes, set boolean not to true and remove the '!'
         * from the expression string afterwards to be able to process the expression correctly. */
        if (expression.startsWith("!") && expression.length() > 1) {
            not = true;
            expression = expression.substring(1);
        }

        /* Expression is what's passed on the right side: Example -> #int = #yaw;
         * Then #yaw would be the expression here. */
        if (Variable.isValidVariableName(expression)) {
            Object variableValue = provider.getVariable(expression, macro);
            if (variableValue != null) {
                if (variableValue instanceof String) {
                    setVariable(expression, (String) variableValue);
                } else if (variableValue instanceof Integer) {
                    setVariable(expression, (Integer) variableValue);
                } else if (variableValue instanceof Boolean) {
                    setVariable(expression, (Boolean) variableValue);
                }
            }
        }

        int intValue;
        /* If a variable has been part of the expression (#i = #int + 3) -> #int will be passed as expression here,
         * and then the variables HashMap is being checked for if #int exists within it. */
        if (variables.containsKey(expression)) {
            intValue = variables.get(expression);
        // If no operator and no parenthesis are found within the expression, this means that the expression should be the final value
        // (no more mathematical operations should be required, hence why we try to parse this value to an Integer now. The catch block will be reached if
        // the input wasn't an integer, for example if you enter #int = "hello"
        } else if (!containsOperator(expression) && containsNoParentheses(expression)) {
            try {
                intValue = Integer.parseInt(expression.replace("¬", "-"));
            } catch (NumberFormatException e) {
                LiteLoaderLogger.warning("Unable to parse value as integer " + e.getMessage() +
                        "; (Additions -> me.trojan.engine.EditedExpressionEvaluator.getValue(String expression, int depth)");
                intValue = 0;
                LiteLoaderLogger.warning("Assigned default value " + intValue + " for entered variable (" + expression + ")");
            }
        } else {
            intValue = this.evaluate(expression, depth + 1);
        }

        if (not)
            intValue = intValue < 1 ? 1 : 0;

        return intValue;
    }

    private boolean containsOperator(String expression) {
        return PATTERN_OPERATOR.matcher(expression).find();
    }

    /**
     * @return true if ( and ) aren't within the expression
     */
    private boolean containsNoParentheses(String expression) {
        return expression.indexOf(40) == -1 || expression.indexOf(41) == -1;
    }
}