package dev.ronnieotieno.imageloader.models

data class ImagesDataClass(
    val categories: List<Category>? = null,
    val color: String? = null,
    val created_at: String? = null,
    val current_user_collections: List<Any>? = null,
    val height: Int? = null,
    val id: String? = null,
    val liked_by_user: Boolean? = null,
    var likes: Int? = null,
    val links: LinksX? = null,
    val urls: Urls? = null,
    val user: User? = null,
    val width: Int? = null
)