package fi.bytecraft.spock.reviewservice

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

import javax.transaction.Transactional

import fi.bytecraft.spock.review.CommentRepository
import fi.bytecraft.spock.review.ReviewService
import fi.bytecraft.spock.review.models.ReactionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
@Sql(scripts = "classpath:add-reaction.sql", executionPhase = BEFORE_TEST_METHOD)
class AddReactionISqlSpec extends Specification {

    @Autowired @Subject
    ReviewService reviewService;
    @Autowired
    CommentRepository commentRepository;

    @Transactional
    def "adding reaction for a comment that has reaction of given type should increase reaction count by one"() {
        given: "reaction to give"
            def reactionToGive = ReactionType.LIKE;

        when: "adding reaction for comment"
            reviewService.addReactionForComment(1L, reactionToGive);

        then: "reaction count should have increased by one"
            def reactedComment = commentRepository.findById(1L).get();
            def reactionThatWasChanged = reactedComment.reactions
                    .find {reaction -> reaction.type == reactionToGive }
            reactionThatWasChanged.amount == 3
    }

}