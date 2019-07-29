package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;
import de.pburke.exceptions.InvalidVariables;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class VariablesTest {
    Variables instance;
    @Before
    public void setUp() throws Exception {
        instance = new Variables();
    }

    @Test
    public void getSplitVariableValid() throws InvalidVariableCreation, InvalidVariables {
        var variables = new ArrayList<>(Arrays.asList(
                new Variable(0, 0),
                new Variable(3, 6),
                new Variable(-6, 4),
                new Variable(-2, 5)
        ));

        var instance = new Variables(variables);

        var variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(1));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(2));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(3));
    }

    @Test(expected = InvalidVariables.class)
    public void getSplitVariableOnlyPointIntervals() throws InvalidVariableCreation, InvalidVariables {
        var variables = new ArrayList<>(Arrays.asList(
            new Variable(0, 0),
            new Variable(3, 3)
        ));

        var instance = new Variables(variables);

        var variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(1));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(2));
    }

    @Test(expected = InvalidVariables.class)
    public void getSplitVariableNoEmptyList() throws InvalidVariables {
        instance.getSplitVariable();
    }

    @Test
    public void isValid() throws InvalidVariableCreation {
        var variable1 = new Variable(0, 0);

        instance = new Variables(variable1);

        assertFalse(instance.isValid());
    }

    @Test
    public void isValidMultipleVars() throws InvalidVariableCreation {
        var variable1 = new Variable(0, 0);
        var variable2 = new Variable(0, 5);
        instance = new Variables(variable1, variable2);

        assertTrue(instance.isValid());
    }

    @Test
    public void isValidMultipleVarsTrue() throws InvalidVariableCreation {
        var variable1 = new Variable(0, 3);
        var variable2 = new Variable(0, 5);
        instance = new Variables(variable1, variable2);

        assertTrue(instance.isValid());
    }

}