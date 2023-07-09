package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainServlet extends HttpServlet {
	private PostController controller;
	private PostService service;
	private PostRepository repository;
	private AnnotationConfigApplicationContext context;

	@Override
	public void init() {
		//final var context = new AnnotationConfigApplicationContext("ru.netology");
		context = new AnnotationConfigApplicationContext("ru.netology");
		controller = context.getBean(PostController.class);
		service = context.getBean(PostService.class);
		repository = context.getBean(PostRepository.class);

		//final var repository = new PostRepository();
		//final var service = new PostService(repository);
		//controller = new PostController(service);
	}
	/*
	public static void main(String[] args) {
		final var context = new AnnotationConfigApplicationContext("ru.netology");
		final var controller = context.getBean("postController");
		final var service = context.getBean("postService");
		final var repository = context.getBean("postRepository");
		//final var isSame = service == context.getBean("PostService");
	}
	*/


	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		// если деплоились в root context, то достаточно этого
		try {
			final var path = req.getRequestURI();
			final var method = req.getMethod();
			// primitive routing
			if (method.equals("GET") && path.equals("/api/posts")) {
				controller.all(resp);
				return;
			}
			if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
				// easy way
				final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
				controller.getById(id, resp);
				return;
			}
			if (method.equals("POST") && path.equals("/api/posts")) {
				controller.save(req.getReader(), resp);
				return;
			}
			if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
				// easy way
				final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
				controller.removeById(id, resp);
				return;
			}
			// resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//resp.setHeader("Content-Type", "application/json");
			//resp.getWriter().print("{\"message\":\"Hello from servelet\"}");
			//resp.getWriter().print("Hello from servelet");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
