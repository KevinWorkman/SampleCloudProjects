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

// Week 3: More advanced JavaScript
// Week 4: Libraries
@WebServlet(name = "WordCountJsonServlet", value = "/word-count-json")
public class WordCountJsonServlet extends HttpServlet{

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

		response.setContentType("application/json");
		response.getWriter().println("[");
		
		String q = "\"";

		boolean prependComma = false;
		
		for(String word : wordCountMap.keySet()) {
			if(wordCountMap.get(word) > 1000) {
				
				if(prependComma) {
					response.getWriter().print(", ");
				}
				response.getWriter().println("[" + q + word + q + ", " + wordCountMap.get(word) + "]");
				
				prependComma = true;
			}
		}
		response.getWriter().println("]");
	}

}
