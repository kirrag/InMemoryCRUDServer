package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

	private final ConcurrentMap<Long, Post> repo; 

	private static AtomicLong nextId = new AtomicLong();

	public PostRepository() {
		this.repo = new ConcurrentHashMap<>() ;
	}

	private long getNextId() {
		return nextId.incrementAndGet();
	}

	public ConcurrentMap<Long, Post> all() {
		return repo;
	}

	public Optional<Post> getById(Long id) {
		return Optional.ofNullable(repo.get(id));
	}

	public Post save(Post post) {
		long id = post.getId();
		if (id == 0) {
			id = getNextId();
			post.setId(id);
			repo.put(id, post);
		} else {
			repo.put(id, post);
		}
		return post;
	}

	public void removeById(long id) {
		repo.remove(id);
	}
}
