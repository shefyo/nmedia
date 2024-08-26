//

//package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.Post

//@Entity
//data class PostEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long,
//    val author: String,
//    val content: String,
//    val published: String,
//    val likedByMe: Boolean,
//    val countlikes: Int = 0,
//    val countreposts: Int = 0,
//    val repostedByMe: Boolean,
//    val countviews: Int = 0,
//    val video: String?,
//    val avatar
//) {
//    fun toDto() = Post(id, author, content, published, likedByMe, countlikes, countreposts, repostedByMe, countviews, video)
//
//    companion object {
//        fun fromDto(dto: Post) =
//            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likes, dto.countreposts, dto.repostedByMe, dto.countviews, dto.video)
//
//    }
//}
//
