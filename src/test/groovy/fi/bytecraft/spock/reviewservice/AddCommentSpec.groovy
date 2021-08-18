package fi.bytecraft.spock.reviewservice

import static ReviewServiceSpecUtils.buildDefaultReview_groovy25

import fi.bytecraft.spock.review.CommentRepository
import fi.bytecraft.spock.review.ErrorRepository
import fi.bytecraft.spock.review.ReviewException
import fi.bytecraft.spock.review.ReviewRepository
import fi.bytecraft.spock.review.ReviewService
import fi.bytecraft.spock.review.models.Error
import fi.bytecraft.spock.review.models.Review
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException
import spock.lang.Specification
import spock.lang.Subject

class AddCommentSpec extends Specification {

    @Subject ReviewService reviewService
    ReviewRepository reviewRepository
    ErrorRepository errorRepository
    CommentRepository commentRepository

    static REVIEW_ID = 1L
    static COMMENT = "That review is very good!!!!"

    def setup() {
        reviewRepository = Mock()
        errorRepository = Mock(ErrorRepository)
        commentRepository = Mock(CommentRepository.class)
        reviewService = new ReviewService(reviewRepository, errorRepository, commentRepository)
    }

    def "addComment() when data source is unavailable throws review exception and creates new error row"() {
        given: "a review for commenting"
        and: "author and body for comment"
            def author = "Antti"

        when: "trying to create the given comment for author"
            reviewService.addComment(REVIEW_ID, author, COMMENT)

        then: "review is found from db"
            1 * reviewRepository.findById(REVIEW_ID) >> Optional.of(buildDefaultReview_groovy25())
        and: "while trying to save comment for review the datasource is not available"
            1 * reviewRepository.save(_ as Review) >> { throw new DataSourceLookupFailureException("Datasource not available") }
        and: "an error-report should be generated to db"
            1 * errorRepository.save(_ as Error)
        and: "review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Commenting is disabled because DBA has broken our connection"
    }

    def "adding a comment without author creates an anonymous user with random integer identifier"() {
        given: "a review for commenting"
        and: "no author, only body for comment"

        when: "trying to create the given comment for author"
            reviewService.addCommentWithoutAuthor(REVIEW_ID, COMMENT)

        then: "review is found from db"
            1 * reviewRepository.findById(REVIEW_ID) >> Optional.of(buildDefaultReview_groovy25())
        and: "comment is saved with anonymous user with random integer identifier"
            1 * reviewRepository.save(_ as Review) >> { args ->
                def reviewWithNewComment = args[0] as Review

                def addedComment = reviewWithNewComment.comments.first()

                assert addedComment.author ==~ /anonymous-teddy-bear-\d+/
            }
    }

}