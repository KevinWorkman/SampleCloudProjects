package io.happycoding.cloudsamples.servlets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Week 2: More advanced Java
@WebServlet(name = "WordCountServlet", value = "/word-count-simple")
public class SimpleWordCountServlet extends HttpServlet{

	private Map<String, Integer> wordCountMap = new HashMap<>();

	@Override
	public void init()
			throws ServletException {

		try {
			byte[] encoded = Files.readAllBytes(Paths.get(getServletContext().getResource("/WEB-INF/dracula.txt").toURI()));
			String text = new String(encoded, Charset.defaultCharset());
			
			// Turn -- into spaces
			text = text.replace("--", " ");
			
			// Strip all punctuation
			text = text.replaceAll("[^a-zA-Z\\s]", "");
			
			// Make everything lowercase
			text = text.toLowerCase();
			
			String[] words = text.split("\\s");
			for(String word : words) {
				if(wordCountMap.containsKey(word)) {
					wordCountMap.put(word, wordCountMap.get(word) + 1);	
				}
				else {
					wordCountMap.put(word, 1);
				}
			}
			
			wordCountMap.remove("");

		}
		catch(IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/plain");

		for(String word : wordCountMap.keySet()) {
			response.getWriter().println(word + ": " + wordCountMap.get(word));
		}
	}

}
