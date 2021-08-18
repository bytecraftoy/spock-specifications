package fi.bytecraft.spock.algorithm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static fi.bytecraft.spock.algorithm.CurlyBracesChecker.hasValidCurlyBraces;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HasValidCurlyBraces {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String nullCode = null;

    @Test
    public void withCodeThatHasNoBraces_returnsTrue() {
        String codeWithoutAnyBraces = "this.code = variable;";

        assertTrue(hasValidCurlyBraces(codeWithoutAnyBraces));
    }

    @Test
    public void withValidCode_thatHasOpeningAndClosingBraces_onTheSameline_returnsTrue() {
        String codeWithValidSingleBraces = "public class JavaClass {}";

        assertTrue(hasValidCurlyBraces(codeWithValidSingleBraces));
    }

    @Test
    public void withInvalidCode_thatHasOneOpeningAndNoClosingBrace_returnsFalse() {
        String codeWithInvalidOnlyOpeningBrace = "public class JavaClass {";

        assertFalse(hasValidCurlyBraces(codeWithInvalidOnlyOpeningBrace));
    }

    @Test
    public void withInvalidCode_thatHasOnlyOneClosingBrace_returnsFalse() {
        String codeWithInvalidOnlyClosingBrace = "public class JavaClass }";

        assertFalse(hasValidCurlyBraces(codeWithInvalidOnlyClosingBrace));
    }

    @Test
    public void withValidCode_thatHasMultipleOpeningAndClosingBraces_onMultipleLines_returnsTrue() {
        String codeWithValidMultiLineMultiBraces =
                "public class JavaClass {" +
                "   JavaClass() { }" +
                "}";

        assertTrue(hasValidCurlyBraces(codeWithValidMultiLineMultiBraces));
    }

    @Test(expected = AlgorithmException.class)
    public void withNullCode_throwsException_way1() {
        hasValidCurlyBraces(nullCode);
    }

    @Test
    public void withNullCode_throwsException_way2() {
        thrown.expect(AlgorithmException.class);
        thrown.expectMessage("Provided code was null!");

        hasValidCurlyBraces(nullCode);
    }

}
