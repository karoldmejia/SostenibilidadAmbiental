package model;

/**
 * Represents a comment made by a user for an interest's point.
 */
public class Comment {
    User user;
    String commentText;

    /**
     * Constructs a comment with associated user and comment text.
     * @param user The user who made the comment.
     * @param commentText The text of the comment.
     */
    Comment(User user, String commentText){
        this.user=user;
        this.commentText=commentText;
    }

    /**
     * Retrieves the name of the user who made the comment.
     * @return The user's name.
     */
    public String getUserName() {
        return user.getUsername();
    }

    /**
     * Retrieves the text of the comment.
     * @return The comment's text.
     */
    public String getCommentText() {
        return commentText;
    }
}

