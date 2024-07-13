package ru.netology.nmedia

import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepositoryInterface
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepositoryInterface {
    override fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toDto()
        }
    }

    override suspend fun getPost(id: Long): Post = withContext(Dispatchers.IO) {
        dao.getPostById(id)
    }

    override suspend fun likeById(id: Long) = withContext(Dispatchers.IO) {
        dao.likeById(id)
    }

    override suspend fun repostById(id: Long) = withContext(Dispatchers.IO) {
        dao.repostById(id)
    }

    override suspend fun save(post: Post) = withContext(Dispatchers.IO) {
        dao.save(PostEntity.fromDto(post))
    }

    override suspend fun removeById(id: Long) = withContext(Dispatchers.IO) {
        dao.removeById(id)
    }
}
