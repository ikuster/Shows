package i.kuster.data.model

data class CommentDeleteResponse(
    @Transient
    var isSuccessful: Boolean? = null,
    @Transient
    var message: String? = null
)