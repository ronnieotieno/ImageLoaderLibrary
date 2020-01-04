package dev.ronnieotieno.imageloader.models

data class ImagesDataClass(
    val categories: List<Category>,
    val color: String,
    val created_at: String,
    val current_user_collections: List<Any>,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val links: LinksX,
    val urls: Urls,
    val user: User,
    val width: Int
)