package fi.bytecraft.spock.review;

import fi.bytecraft.spock.review.models.Comment;
import fi.bytecraft.spock.review.models.Error;
import fi.bytecraft.spock.review.models.ReactionType;
import fi.bytecraft.spock.review.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ErrorRepository errorRepository;
    private CommentRepository commentRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         ErrorRepository errorRepository,
                         CommentRepository commentRepository) {
        this.reviewRepository = reviewRepository;
        this.errorRepository = errorRepository;
        this.commentRepository = commentRepository;
    }

    public void addComment(Long reviewId, String author, String comment) throws ReviewException {
        Optional<Review> reviewToAddComment = reviewRepository.findById(reviewId);

        if (reviewToAddComment.isEmpty())
            throw new ReviewException(String.format("Review with id %d not found", reviewId));

        Review reviewToComment = reviewToAddComment.get();

        if (!StringUtils.hasText(comment)) {
            throw new ReviewException("Comment can't be blank");
        }

        Comment commentToAdd = createCommentWithAuthorAndBody(author, comment);

        reviewToComment.getComments().add(commentToAdd);

        try {
            reviewRepository.save(reviewToComment);
        } catch (DataSourceLookupFailureException ex) {
            Error error = new Error();
            error.setReason("Connection error");
            errorRepository.save(error);
            throw new ReviewException("Commenting is disabled because DBA has broken our connection", ex);
        }
    }

    private Comment createCommentWithAuthorAndBody(String author, String comment) {
        Comment commentToAdd = new Comment();
        commentToAdd.setAuthor(author);
        commentToAdd.setBody(comment);
        return commentToAdd;
    }

    public void addCommentWithoutAuthor(Long reviewId, String comment) throws ReviewException {
        Optional<Review> reviewToAddComment = reviewRepository.findById(reviewId);

        if (reviewToAddComment.isEmpty())
            throw new ReviewException(String.format("Review with id %d not found", reviewId));

        Review reviewToComment = reviewToAddComment.get();

        if (!StringUtils.hasText(comment)) {
            throw new ReviewException("Comment can't be blank");
        }

        Comment commentToAdd = createCommentWithAnonymousAuthor(comment);

        reviewToComment.getComments().add(commentToAdd);

        reviewRepository.save(reviewToComment);
    }

    private Comment createCommentWithAnonymousAuthor(String comment) {
        Comment commentToAdd = new Comment();
        commentToAdd.setAuthor(String.format("anonymous-teddy-bear%d", UUID.randomUUID().getLeastSignificantBits()));
        commentToAdd.setBody(comment);
        return commentToAdd;
    }

    public void addReactionForComment(Long commentId, ReactionType type) throws ReviewException {
        if (type == null)
            throw new ReviewException("No reaction type given for comment");

        Optional<Comment> commentForReaction = commentRepository.findById(commentId);
        if (commentForReaction.isEmpty())
            throw new ReviewException(String.format("Comment with id %d not found", commentId));

        Comment reactionComment = commentForReaction.get();
        reactionComment.addReaction(type);
        commentRepository.save(reactionComment);
    }

}
