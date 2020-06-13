package ua.external.service.validators;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.external.service.validators.NameValidator.NAME_VALIDATOR;

public class NameValidatorTest {

    @Test
    public void ShouldReturnTrueForValidNames() {
        boolean actual = NAME_VALIDATOR.isValid("Alex");
        assertTrue(actual);
    }

    @Test
    public void ShouldReturnFalseForEmptyString() {
        boolean actual = NAME_VALIDATOR.isValid("");
        assertFalse(actual);
    }

    @Test
    public void ShouldReturnFalseForStringWithOnlySpaces() {
        boolean actual = NAME_VALIDATOR.isValid("  ");
        assertFalse(actual);
    }

    @Test
    public void ShouldReturnFalseForNull() {
        boolean actual = NAME_VALIDATOR.isValid(null);
        assertFalse(actual);
    }

}
