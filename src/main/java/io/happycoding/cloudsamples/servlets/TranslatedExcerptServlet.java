package io.happycoding.cloudsamples.servlets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;

// Week 4: Libraries
@WebServlet(name = "TranslatedExcerptServlet", value = "/translated-excerpt")
public class TranslatedExcerptServlet extends HttpServlet{

	private String text;

	@Override
	public void init()
			throws ServletException {

		try {
			byte[] encoded = Files.readAllBytes(Paths.get(getServletContext().getResource("/WEB-INF/dracula.txt").toURI()));
			text = new String(encoded, Charset.defaultCharset());

			// Get rid of weird newlines
			text = text.replace("\r\n", "\n");
		}
		catch(IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String englishExcerpt = getRandomExcerpt();

		Translate translate = TranslateOptions.getDefaultInstance().getService();
		String spanishExcerpt =
				translate.translate(
						englishExcerpt,
						TranslateOption.sourceLanguage("en"),
						TranslateOption.targetLanguage("es"), 
						TranslateOption.format("text"))
				.getTranslatedText();


		System.out.println(englishExcerpt);
		System.out.println(spanishExcerpt);
		
		String q = "\"";
		
		// Escape quotation marks before using in JSON
		englishExcerpt = englishExcerpt.replace(q, "\\\"");
		spanishExcerpt = spanishExcerpt.replace(q, "\\\"");
		
		// Escape newlines before using in JSON
		englishExcerpt = englishExcerpt.replace("\n", "\\n");
		spanishExcerpt = spanishExcerpt.replace("\n", "\\n");
		
		
		String json = "{";
		json += q + "english" + q + ": ";
		json += q + englishExcerpt + q;
		json += ", ";
		json += q + "spanish" + q + ": ";
		json += q + spanishExcerpt + q;
		json += "}";

		response.setContentType("application/json");
		response.getWriter().println(json);
	}

	private String getRandomExcerpt() {
		// Get a random start position
		int start = (int)(Math.random() * (text.length() - 500));

		// Chop off anything before that start position
		String excerpt = text.substring(start);

		// Chop off anything before the first paragraph break. This starts the excerpt on a new paragraph.
		excerpt = excerpt.substring(excerpt.indexOf("\n\n") + 2);

		// Cut the excerpt down to 500 characters
		excerpt = excerpt.substring(0, 500);

		// Chop off anything after the last paragraph break or sentence, whichever makes for a longer excerpt
		int lastParagraphBreakIndex = excerpt.lastIndexOf("\n\n");
		int lastSentenceIndex = excerpt.lastIndexOf(". ");
		excerpt = excerpt.substring(0, lastParagraphBreakIndex > lastSentenceIndex ? lastParagraphBreakIndex : lastSentenceIndex + 2);	

		return excerpt;
	}

}
