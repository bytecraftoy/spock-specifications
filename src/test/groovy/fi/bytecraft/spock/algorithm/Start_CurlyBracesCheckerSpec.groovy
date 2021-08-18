package fi.bytecraft.spock.algorithm

import static fi.bytecraft.spock.algorithm.CurlyBracesChecker.hasValidCurlyBraces

import spock.lang.Specification

class Start_CurlyBracesCheckerSpec extends Specification {

    def "valid curly braces with code that has no braces returns true"() {
        expect: "no braces is valid"
            String codeWithoutAnyBraces = "this.code = variable;";

            hasValidCurlyBraces(codeWithoutAnyBraces) == true
    }

    def "valid curly braces with valid code that has opening and closing braces on the same line returns true"() {
        expect: "opening and closing brace is valid"
            String codeWithValidSingleBraces = "public class JavaClass {}";

            hasValidCurlyBraces(codeWithValidSingleBraces)
    }

    def "valid curly braces with invalid code that has opening brace but no closing one returns false"() {
        expect: "only opening brace is invalid"
            String codeWithInvalidOnlyOpeningBrace = "public class JavaClass {";

            !hasValidCurlyBraces(codeWithInvalidOnlyOpeningBrace)
    }

    def "valid curly braces with invalid code that has only closing brace returns false"() {
        expect: "only closing brace is invalid"
            String codeWithInvalidOnlyClosingBrace = "public class JavaClass }";

            !hasValidCurlyBraces(codeWithInvalidOnlyClosingBrace);
    }

    def "valid curly braces with valid code that has multiple opening and closing braces on multiple lines returns true"() {
        expect: "multiple opening and closing braces on multiple lines is valid"
            String codeWithValidMultiLineMultiBraces =
                    "public class JavaClass {" +
                            "   JavaClass() { }" +
                            "}";

            hasValidCurlyBraces(codeWithValidMultiLineMultiBraces);
    }

    def "valid curly braces with null code throws exception"() {
        given: "code as null"
            String nullCode = null

        when: "testing valid curly braces with null code"
            hasValidCurlyBraces(nullCode)

        then: "exception is thrown"
            def ex = thrown AlgorithmException
            ex.message == "Provided code was null!"
    }

}