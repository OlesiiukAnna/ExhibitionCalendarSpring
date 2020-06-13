package ua.external.service.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.external.service.validators.PhoneValidator.PHONE_VALIDATOR;

public class PhoneValidatorTest {

    @Test
    public void shouldReturnTrueWhenNumberContainsPlus(){
        boolean actual = PHONE_VALIDATOR.isValid("+380501234567");
        assertTrue(actual);
    }

    @Test
    public void shouldReturnFalseWhenNumberContainsOnlyNumbers(){
        boolean actual = PHONE_VALIDATOR.isValid("380501234567");
        assertFalse(actual);
    }

    @Test
    public void shouldReturnFalseWhenNumberContainsBrackets(){
        boolean actual = PHONE_VALIDATOR.isValid("+38(050)1234567");
        assertFalse(actual);
    }

    @Test
    public void shouldReturnFalseWhenNumberContainsMinusAsSplitter(){
        boolean actual = PHONE_VALIDATOR.isValid("+38-050-123-45-67");
        assertFalse(actual);
    }

    @Test
    public void shouldReturnFalseWhenNumberContainsSpaceAsSplitter(){
        boolean actual = PHONE_VALIDATOR.isValid("+38 050 123 45 67");
        assertFalse(actual);
    }

    @Test
    public void shouldReturnFalseForNull() {
        String phone = null;
        boolean actual = PHONE_VALIDATOR.isValid(phone);
        assertFalse(actual);
    }

    @Test
    public void shouldReturnFalseForPhoneLessThanThreeNumbers() {
        boolean actual = PHONE_VALIDATOR.isValid("+12");
        assertFalse(actual);
    }
}
