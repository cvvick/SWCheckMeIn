package com.swcheckmein.airport;

import java.lang.NullPointerException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
 * Class the encapsulate the retrieval of Airport information
 * 
 * It first checks to see if the airport is cached locally in the database. If not, it retrieves the
 * information from the FlightStats service
 */
public class Airport {
	/* Web service end point for the Airport REST API */
	private static final String flightStatsEndpoint = "	https://api.flightstats.com/flex/airports/rest/v1/json/iata/";
	private static final String appId = "5a67d947";
	private static final String appKey = "57b4712569ec1747df321829c3b46b80";
	
	
	private final String fs; 			/*	The FlightStats code for the airport, globally unique across time.*/
	private final String iata;			/* The IATA code for the airport */
	private final String icao;			/* The ICAO code for the airport */
	private final String faa;			/* The FAA code for the airport */
	private final String name;			/* The name of the airport */
	private final String street1;		/* The street address of the airport, part 1 */
	private final String street2; 		/* Street address of the airport, part 2 */
	private final String city;			/* The city with which the airport is associated */
	private final String cityCode;		/* The city code with which the airport is associated */
	private final String stateCode; 	/* State in which the airport is located */
	private final String postalCode; 	/* The postal code in which the airport resides */
	private final String countryCode; 	/* The code for the country in which the airport is located */
	private final String countryName; 	/* The name of the country in which the Airport is located. */
	private final String regionName; 	/* The name of the region in which the Airport is located */
	private final String timeZoneRegionName; /* The name of the Time Zone region in which the Airport is located */
	private final String weatherZone; 	/* The NOAA weather zone (US only) in which the Airport is located */
	private final Double latitude; 		/* The latitude of the airport in decimal degrees.*/
	private final Double longitude;		/* The longitude of the airport in decimal degrees.*/
	
	private Airport()
	{}
	
	public static final Airport lookup ( String iataCode )
	{
		/* Check if we've got an argument */
		if ( iataCode == null )
			throw new NullPointerException();
		
		/* TODO: Check if we've already retrieved the airport before and retrieve from the database */
		
		/* retrieve the data from the FlightStats web service */
		try
		{
			URL fsAirportRequest = new URL ( flightStatsEndpoint + iataCode + "?appId=" + appId + "&appKey=" +appKey );
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fsAirportRequest.openStream()));
		}
		catch ( MalformedURLException e)
		{}
		catch ( IOException e )
		{}
		
		
		return new Airport();
	}
}
