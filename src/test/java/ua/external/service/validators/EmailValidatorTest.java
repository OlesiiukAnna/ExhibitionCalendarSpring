package ua.external.service.validators;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static ua.external.service.validators.EmailValidator.EMAIL_VALIDATOR;

public class EmailValidatorTest {

    private List<String> validEmails;
    private List<String> invalidEmails;

    @Before
    public void setUp() {
        validEmails = List.of("simple@example.com",                 // 0
                "very.common@example.com",                          // 1
                "disposable.style.email.with-symbol@example.com",   // 2
                "other.email-with-hyphen@example.com",              // 3
                "fully-qualified-domain@example.com",               // 4
                "x@example.com",                                    // 5
                "example-indeed@strange-example.com",               // 6
                "admin@mailserver1.kk",                             // 7
                "example@s.exam",                                   // 8
                "66@example.org",                                   // 9
                "john..doe@example.org",                            // 10
                "mailhost-username@example.org",                    // 11
                "user%example.com@example.org");                    // 12

        invalidEmails = List.of("Abc.example.com",                  // 0
                "A@b@c@example.com",                                // 1
                "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",            // 2
                "just\"not\"right@example.com",                     // 3
                "this is\"not\\allowed@example.com",                // 4
                "this\\ still\\\"not\\\\allowed@example.com",       // 5
                "1234567890123456789012345678901234567890123456789012345678901234+x@example.com", // 6
                "", " "); // 7, 8
    }

    @Test
    public void shouldReturnTrueForValidEmails() {
        List<Boolean> actual = new ArrayList<>();
        for (String email : validEmails) {
            actual.add(EMAIL_VALIDATOR.isValid(email));
        }
        List<Boolean> expected = List.of(true, true, true,
                true, true, true, true, true,
                true, true, true, true, true);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void shouldReturnFalseForInvalidEmails() {
        List<Boolean> actual = new ArrayList<>();
        for (String email : invalidEmails) {
            boolean result = EMAIL_VALIDATOR.isValid(email);
            actual.add(result);
        }
        List<Boolean> expected = List.of(false, false, false,
                false, false, false, false, false, false);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void shouldReturnFalseForNull() {
        String email = null;
        boolean actual = EMAIL_VALIDATOR.isValid(email);
        assertFalse(actual);
    }

}
