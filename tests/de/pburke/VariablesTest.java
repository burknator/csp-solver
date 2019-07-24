package de.pburke;

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
    public void getSplitVariableValid() throws Exception {
        var variables = new ArrayList<>(Arrays.asList(
                new Variable(0, 0),
                new Variable(3, 6),
                new Variable(-6, 4),
                new Variable(-2, 5)
        ));

        instance.addAll(variables);

        var variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(1));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(2));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(3));
    }

    @Test(expected = InvalidVariables.class)
    public void getSplitVariableOnlyPointIntervals() throws Exception {
        var variables = new ArrayList<>(Arrays.asList(
            new Variable(0, 0),
            new Variable(3, 3)
        ));

        instance.addAll(variables);

        var variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(1));

        variable = instance.getSplitVariable();
        assertEquals(variable, variables.get(2));
    }

    @Test(expected = InvalidVariables.class)
    public void getSplitVariableNoEmptyList() throws Exception {
        instance.getSplitVariable();
    }
}