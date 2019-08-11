package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;

import java.util.ArrayList;

public class Parser {
    private enum State {
        DECL,
        FORMULA;
    }

    private ArrayList varList = new ArrayList();

    public Parser() {};

    public Formula parse(String input) {
        int i = 0; //position in input string
        int j = 0; //length of the next piece of information
        int min = 0; //variable.min
        int max = 0; //variable.max
        String varName = "";    //variable.name
        String simpleBoundX = "";
        String simpleBoundY = "";
        int simpleBoundK = 0;

        input = input.replaceAll("[\\n\\r\\t]+", ""); //remove format symbols

        State currentState = null;
        ArrayList sbList = new ArrayList(); //list of SimpleBounds for one Constraint
        ArrayList coList = new ArrayList(); //list of Constraints for Formula

        i = skipWhiteSpace(input, i);
        if (input.substring(i, i+4).equals("DECL")) {
            i += 4; //skip "DECL"
            currentState = State.DECL;
        } else {
            Logger.log("Invalid input.");
        }

        while (i < input.length() ) {
            i = skipWhiteSpace(input, i);

            switch(currentState) {

                case DECL:
                    j = 0;
                    j = determineCharacterCount(input, i);
                    varName = input.substring(i, i+j);
                    i += j;
                    i = skipWhiteSpace(input, i);
                    j = 0;
                    j = determineCharacterCount(input, i);
                    min = Integer.parseInt(input.substring(i, i+j));
                    i += j;
                    i = skipWhiteSpace(input, i);
                    j = 0;
                    j = determineCharacterCount(input, i);
                    max = Integer.parseInt(input.substring(i, i+j));
                    try {
                        varList.add(new Variable(varName, min, max));
                    } catch (InvalidVariableCreation e) {
                        Logger.log("Invalid variables.");
                    }
                    i += j+1; //skip ";"
                    i = skipWhiteSpace(input, i);

                    if (input.substring(i, i+7).equals("FORMULA")) {
                        currentState = State.FORMULA;
                        i += 7; //skip "FORMULA"
                        i = skipWhiteSpace(input, i);
                    }
                    break;

                case FORMULA:
                     j = 0;
                     j = determineCharacterCount(input, i);
                     simpleBoundX = input.substring(i, i+j);
                     i += j;

                     i = skipWhiteSpace(input, i);
                     i += 2; //skip "greater than"
                     i = skipWhiteSpace(input, i);

                     j = 0;
                     j= determineCharacterCount(input, i);
                     simpleBoundY = input.substring(i, i+j);
                     i += j;
                     i = skipWhiteSpace(input, i);

                     if (input.substring(i, i+1).equals(";")) { //pure Integer bound?
                         if (simpleBoundY.substring(0, 1).equals("-")) {
                             simpleBoundK = (-1) * Integer.parseInt(simpleBoundY);
                         } else {
                             simpleBoundK = Integer.parseInt(simpleBoundY);
                         }
                         i++;
                         if (i+1 >= input.length()) { //end of input-String?
                             sbList.add(createSimpleBound(simpleBoundX, simpleBoundY, simpleBoundK, varList));
                             coList.add(createConstraint(sbList));
                             sbList.clear();
                             break;
                         } else {
                             sbList.add(createSimpleBound(simpleBoundX, simpleBoundY, simpleBoundK, varList));
                             coList.add(createConstraint(sbList));
                             sbList.clear();
                             i = skipWhiteSpace(input, i);
                         }
                     } else {
                         i++; //skip "+"
                         i = skipWhiteSpace(input, i);

                         j = 0;
                         j = determineCharacterCount(input, i);
                         if (input.substring(i, i + 1).equals("-")) {
                             simpleBoundK = (-1) * Integer.parseInt(input.substring(i + 1, i + j));
                         } else {
                             simpleBoundK = Integer.parseInt(input.substring(i, i + j));
                         }
                         sbList.add(createSimpleBound(simpleBoundX, simpleBoundY, simpleBoundK, varList));
                         i += j;
                         i = skipWhiteSpace(input, i);
                     }

                     if (input.substring(i, i+1).equals("v")) {
                         i++; //skip "v"
                         i = skipWhiteSpace(input, i);
                     } else if (input.substring(i, i+1).equals(";")) {
                         coList.add(createConstraint(sbList));
                         sbList.clear();
                         i++; //skip ";"
                         if (i >= input.length()) { //end of String?
                             break;
                         } else {
                             i = skipWhiteSpace(input, i);
                         }
                     }
                     break;
            }
        }
        Formula formula = createFormula(coList);
        return formula;
    }

    private int skipWhiteSpace(String input, int i) {
        while (input.substring(i, i+1).equals(" ")) {
            i++;
        }
        return i;
    }

    private int determineCharacterCount(String input, int i){
        int j = 0;
        while (!(input.substring(i, i+1).equals(" ")) && !(input.substring(i, i+1).equals(";")) ){
            i++;
            j++;
        }
        return j;
    }

    private SimpleBound createSimpleBound(String sbx, String sby, int sbk, ArrayList varList) {
        int i = 0;
        int j = 0;

        Variable[] varArray = (Variable[]) varList.toArray(new Variable[varList.size()]);

        while (!(varArray[i].getName().equals(sbx))) {
            i++;

            if (i >= varArray.length) { //varName not found -> pure Integer bound
                break;
            }
        }

        while (!(varArray[j].getName().equals(sby))) {
            j++;

            if (j >= varArray.length) { //varName not found -> pure Integer bound
                break;
            }
        }

        if ((i >= varArray.length) || (j >= varArray.length)) {
            int sbxInt = 0;
            if (sbx.substring(0, 1).equals("-")) {
                sbxInt = (-1) * Integer.parseInt(sbx.substring(1, sbx.length()));
            } else {
                sbxInt = Integer.parseInt(sbx);
            }
            SimpleBound sb = new SimpleBound(sbxInt, sbk);
            return sb;
        } else {
            SimpleBound sb = new SimpleBound(varArray[i], varArray[j], sbk);
            return sb;
        }
    }

    private Constraint createConstraint(ArrayList sbList) {
        SimpleBound[] sbArray = (SimpleBound[]) sbList.toArray(new SimpleBound[sbList.size()]);
        Constraint constraint = new Constraint(sbArray);
        return constraint;
    }

    private Formula createFormula(ArrayList coList) {
        Constraint[] coArray = (Constraint[]) coList.toArray(new Constraint[coList.size()]);
        Formula formula = new Formula(coArray);
        return formula;
    }

    public ArrayList getVariableList() {
        return varList;
    }
}