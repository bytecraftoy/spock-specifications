package fi.bytecraft.spock.algorithm

import spock.lang.Unroll

import static CurlyBracesChecker.hasValidCurlyBraces;

import spock.lang.Specification

class CurlyBracesCheckerSpec extends Specification {

    static codeWithoutAnyBraces = "this.code = variable;"
    static codeWithValidSingleBraces = "public class JavaClass {}"
    static codeWithInvalidOnlyOpeningBrace = "public class JavaClass {"
    static codeWithInvalidOnlyClosingBrace = "public class JavaClass }"
    static codeWithValidMultiLineMultiBraces = """\
            public class JavaClass {
                JavaClass() { }
            }
            """

    @Unroll
    def "hasValidCurlyBraces() with #contextDesc returns #outcome"() {
        expect: "#contextDesc has #validity curly braces"
            hasValidCurlyBraces(codeToCheck) == outcome

        where:
        codeToCheck                         | contextDesc                           || outcome  | _
        codeWithoutAnyBraces                | "code without any braces"             || true     | _
        codeWithValidSingleBraces           | "code with single braces"             || true     | _
        codeWithValidMultiLineMultiBraces   | "code with braces on multiple lines"  || true     | _
        codeWithInvalidOnlyOpeningBrace     | "code with only single opening brace" || false    | _
        codeWithInvalidOnlyClosingBrace     | "code with only single closing brace" || false    | _

        validity = booleanToValid(outcome)
    }

    def "hasValidCurlyBraces() with null code throws exception"() {
        given: "code as null"
            String nullCode = null
        when: "testing valid curly braces with null code"
            hasValidCurlyBraces(nullCode)
        then: "exception is thrown"
            def ex = thrown AlgorithmException
            ex.message == "Provided code was null!"
    }

    private static booleanToValid(Boolean value) {
        value ? "valid" : "invalid"
    }

}