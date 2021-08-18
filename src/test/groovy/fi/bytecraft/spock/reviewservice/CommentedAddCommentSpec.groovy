package fi.bytecraft.spock.reviewservice

import fi.bytecraft.spock.review.CommentRepository
import fi.bytecraft.spock.review.ReviewException
import fi.bytecraft.spock.review.ReviewRepository
import fi.bytecraft.spock.review.ReviewService
import fi.bytecraft.spock.review.models.Error
import fi.bytecraft.spock.review.models.Review
import fi.bytecraft.spock.review.ErrorRepository
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException
import spock.lang.Specification
import spock.lang.Subject

import static ReviewServiceSpecUtils.buildDefaultReview_groovy25

//name can be anything, just remember to extend specification at some point of class inheritance tree for initing Spock
//for your test case (specification file)
class CommentedAddCommentSpec extends Specification {

    @Subject ReviewService reviewService //subject can be used for highlighting the class under test
    ReviewRepository reviewRepository
    ErrorRepository errorRepository
    CommentRepository commentRepository

    static REVIEW_ID = 1L
    static COMMENT = "That review is very good!!!!"

    def setup() {
        reviewRepository = Mock() //simplest way of creating a mock, if the variable type is defined
        errorRepository = Mock(ErrorRepository) // you can use this with def created variables
                                                // e.g. def errorRepository = Mock(ErrorRepository)
        commentRepository = Mock(CommentRepository.class) // or this, the upper ^^ is shorthand for this
        reviewService = new ReviewService(reviewRepository, errorRepository, commentRepository) //create the tested service with dependencies substituted with mocks
    }

    //In groovy, method names can be defined with GString objects (strings). This is a powerful feature for testing.
    //The naming format is the same as with examples seen earlier with xUnit testing frameworks
    //   action       | context                      | expected outcome      | expected outcome
    def "addComment() when data source is unavailable throws review exception and creates new error row"() {
        given: "a review for commenting"
        and: "author and body for comment"
        def author = "Antti"

        when: "trying to create the given comment for author"
        reviewService.addComment(REVIEW_ID, author, COMMENT)

        then: "review is found from db"
        //The wiring of mocks and stubs can happen with Spock after the called action instead of Mockito way of wiring before the action. This enables describing the natural flow inside the action
        //In here, wiring the mock + verifying mock interaction + stubbing return value happens in one line
        // verifying part: 1 * <-- exactly one time
        //  wiring the mock: reviewRepository.findById(reviewId), when the mock object reviewRepository is called with the exact parameter reviewId (1L)
        //    stubbing: happens with '>>' <-- then return Optional default review
        1 * reviewRepository.findById(REVIEW_ID) >> Optional.of(buildDefaultReview_groovy25())
        and: "while trying to save comment for review the datasource is not available"
        //here we substitute the return value with closure (lambda in java) where an exception is thrown
        1 * reviewRepository.save(_ as Review) >> { throw new DataSourceLookupFailureException("Datasource not available") }
        and: "an error-report should be generated to db"
        //verify mock interaction happens, no stubbing of value
        1 * errorRepository.save(_ as Error)
        and: "review exception is thrown"
        def ex = thrown ReviewException // assign the exception into variable if we need to check exception message
                                        // if we are only interested about exception type, use thrown ReviewException
        ex.message == "Commenting is disabled because DBA has broken our connection" // check the exception message
    }

    //The naming can also be very behavior describing, without any hard take on the actual method under test
    //This provides more robustness for tests when refactoring the actual method name. Behaviour doesn't really change as often as the concrete naming
    def "adding a comment without author creates an anonymous user with random integer identifier"() {
        given: "a review for commenting"
        and: "no author, only body for comment"

        when: "trying to create the given comment for author"
            reviewService.addCommentWithoutAuthor(REVIEW_ID, COMMENT)

        then: "review is found from db"
        1 * reviewRepository.findById(REVIEW_ID) >> Optional.of(buildDefaultReview_groovy25())
        and: "comment is saved with anonymous user with random integer identifier"
        1 * reviewRepository.save(_ as Review) >> { args -> //this is called argument capturing
            def reviewWithNewComment = args[0] as Review //we can fetch the input that mock receives and do anything with it

            def addedComment = reviewWithNewComment.comments.first()

            assert addedComment.author ==~ /anonymous-teddy-bear-\d+/ //this is especially useful for situations when we have a void method, that passes its results to some dependency
                                                                      //we can intercept that call and see what the method did, even though it didn't return anything
        }
    }

}