package fi.bytecraft.spock.reviewservice

import fi.bytecraft.spock.review.ReviewException
import fi.bytecraft.spock.review.ReviewRepository
import fi.bytecraft.spock.review.ReviewService
import fi.bytecraft.spock.review.models.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.transaction.Transactional

import static ReviewServiceSpecUtils.buildDefaultReview_groovy25

@SpringBootTest
class AddCommentDataDrivenISpec extends Specification {

    @Autowired @Subject ReviewService reviewService

    @Autowired ReviewRepository reviewRepository

    Review persistedReview
    def COMMENT_AUTHOR = "Antti A"
    def COMMENT = "Your review2 sucked big time!"

    def setup() {
        persistedReview = createDefaultReview()
    }

    @Transactional
    def "addComment() with valid comment that has persists the comment to given review"() {
        given: "a persisted review"

        expect: "no comments exists for the created review"
            persistedReview.comments.isEmpty()

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "a new comment is added for review"
            reviewRepository.findById(persistedReview.id).get().comments.size() == 1
    }

    @Transactional
    def "addComment() with valid comment that has author sets the author and body for comment"() {
        given: "a persisted review"

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "author and body are set for comment"
            with(reviewRepository.findById(persistedReview.id).get().comments.first()) {
                author == COMMENT_AUTHOR
                body == COMMENT
            }
    }

    def "addComment() for non existing review throws review exception"() {
        given: "nonexisting review id"
            def nonExistingReviewId = 666L
        when: "adding comment to non existing review"
            reviewService.addComment(nonExistingReviewId, COMMENT_AUTHOR, COMMENT)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Review with id $nonExistingReviewId not found"
    }

    @Transactional
    @Unroll
    def "addComment() with #commentDescription comment throws review exception"() {
        given: "a persisted review"

        when: "trying to add the #commentDescription comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, comment)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"

        where:
        comment | commentDescription
        ""      | "blank"
        null    | "null"
    }


    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview_groovy25())
    }

}