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
		context = new AnnotationConfigApplicationContext("ru.netology");
		controller = context.getBean(PostController.class);
	}

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
