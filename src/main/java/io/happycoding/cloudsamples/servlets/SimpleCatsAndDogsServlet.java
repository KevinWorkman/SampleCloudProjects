package io.happycoding.cloudsamples.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Week 2: More advanced Java
@WebServlet(name = "SimpleCatsAndDogsServlet", value = "/cats-vs-dogs-simple")
public class SimpleCatsAndDogsServlet extends HttpServlet{

	Map<String, StateStatistic> stateMap = new HashMap<>();

	@Override
	public void init()
			throws ServletException {

		Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/cats-vs-dogs.csv"));

		// Skip the first line
		scanner.nextLine();

		// Read in the CSV file line by line
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] cells = line.split(";");
			String state = cells[0];
			double dogPercent = Double.valueOf(cells[4]);
			double catPercent = Double.valueOf(cells[8]);
			stateMap.put(state, new StateStatistic(state, dogPercent, catPercent));
		}
		
		scanner.close();

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/plain");

		for(String state : stateMap.keySet()) {
			StateStatistic stateStat = stateMap.get(state);
			response.getWriter().println(state + " is " + stateStat.dogPercent + "% dogs and " 
					+ stateStat.catPercent + "% cats.");
		}
	}

	private static class StateStatistic{
		String state;
		double dogPercent;
		double catPercent;

		public StateStatistic(String state, double dogPercent, double catPercent) {
			this.state = state;
			this.dogPercent = dogPercent;
			this.catPercent = catPercent;
		}
	}

}
