package com.example.runningapp.Data

object CommentData {

    private val comments: MutableList<Comments> = mutableListOf(
        Comments(1, 2, 1, "2021-01-01", "Good run!, Nice run!, Good run!, blal,rioenciuencrenceir"),
        Comments(2, 2, 1, "2021-01-01", "Good run!"),
        Comments(2, 2, 1, "2021-01-01", "Nice run!"),
        Comments(3, 1, 2, "2021-01-02", "Nice ride!"),
        Comments(4, 2, 2, "2021-01-02", "Good ride!"),
        Comments(5, 1, 3, "2021-01-03", "Long ride!"),
        Comments(6, 2, 3, "2021-01-03", "Good ride!"),
        Comments(7, 1, 4, "2021-01-04", "Quick run!"),
        Comments(8, 2, 4, "2021-01-04", "Nice run!"),
    )

    fun getCommentsByActivityId(activityId: Int): List<Comments> {
        comments.sortByDescending { it.date }
        return comments.filter { it.activityId == activityId }
    }

    fun addComment(comment: Comments) {
        comments.add(comment)
    }
}