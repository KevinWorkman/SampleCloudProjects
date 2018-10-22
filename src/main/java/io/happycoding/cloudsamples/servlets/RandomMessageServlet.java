package io.happycoding.cloudsamples.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Week 0 and 1
@WebServlet(name = "RandomMessageServlet", value = "/random-message")
public class RandomMessageServlet extends HttpServlet {

	private String[] messages = {
			"The first programmer was a woman named Ada Lovelace who was born in 1815!",
			"Java was designed by James Gosling in 1995!",
			"JavaScript was designed by Brendan Eich in 1995!"
	};

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/plain");

		String message = messages[(int)(messages.length * Math.random())];

		response.getWriter().println(message);
	}
}