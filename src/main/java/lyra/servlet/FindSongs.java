package lyra.servlet;

import java.io.IOException;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/results")
public class FindSongs extends HttpServlet {
	
//	protected SongsDao songsDao;
//	
//	@Override
//	public void init() throws ServletException {
//		songsDao = SongsDao.getInstance();
//	}
	
	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {

	        // Get all selected inputs
		 	String[] selectedEmotion = req.getParameterValues("emotion");
	        String[] selectedGenres = req.getParameterValues("genre");
	        String[] selectedActivity = req.getParameterValues("activity");
	        int popularity = Integer.parseInt(req.getParameter("popularity"));
	        int energy = Integer.parseInt(req.getParameter("energy"));
	        int danceability = Integer.parseInt(req.getParameter("danceability"));
	        int positivity = Integer.parseInt(req.getParameter("positivity"));
	        int instrumentalness = Integer.parseInt(req.getParameter("instrumentalness"));

	        // need to process the inputs
	        // currently we are only passing them to results.jsp
	        req.setAttribute("selectedGenres", selectedEmotion);
	        req.setAttribute("selectedGenres", selectedGenres);
	        req.setAttribute("selectedGenres", selectedActivity);
	        req.setAttribute("popularity", popularity);
	        req.setAttribute("energy", energy);
	        req.setAttribute("danceability", danceability);
	        req.setAttribute("positivity", positivity);
	        req.setAttribute("instrumentalness", instrumentalness);


	        // forward to results.jsp
	        req.getRequestDispatcher("/results.jsp").forward(req, resp);
	    }
}
