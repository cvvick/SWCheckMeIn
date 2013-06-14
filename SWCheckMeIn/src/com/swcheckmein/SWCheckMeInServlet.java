package com.swcheckmein;
import java.io.IOException;

import javax.servlet.http.*;

import com.swcheckmein.airport.Airport;

@SuppressWarnings("serial")
public class SWCheckMeInServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");		
		Airport airport = Airport.lookup("PDX");
		resp.getWriter().println("fs = " + airport.getFs());
		resp.getWriter().println("iata = " + airport.getIata());	
		resp.getWriter().println("icao = " + airport.getIcao());
		resp.getWriter().println("faa = " + airport.getFaa());
		resp.getWriter().println( "name = " + airport.getName());
		resp.getWriter().println( "street1 = " + airport.getStreet1());
		resp.getWriter().println( "street2 = " + airport.getStreet2());
		resp.getWriter().println( "city = " + airport.getCity());
		resp.getWriter().println( "cityCode = " + airport.getCityCode());
		resp.getWriter().println( "stateCode = " + airport.getStateCode());
		resp.getWriter().println( "postalCode = " + airport.getPostalCode());
		resp.getWriter().println( "countryCode = " + airport.getCountryCode());
		resp.getWriter().println( "countryName = " + airport.getCountryName());
		resp.getWriter().println( "regionName = " + airport.getRegionName());
		resp.getWriter().println( "timeZoneRegionName = " + airport.getTimeZoneRegionName());
		resp.getWriter().println( "weatherZone = " + airport.getWeatherZone());
		resp.getWriter().println( "latitude = " + String.valueOf( airport.getLatitude()));
		resp.getWriter().println( "longitude =" + String.valueOf( airport.getLongitude() ));
	}
}
