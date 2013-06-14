package com.swcheckmein.airport;

import java.lang.NullPointerException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.swcheckmein.EMF;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.EntityManager;
/*
 * Class the encapsulate the retrieval of Airport information
 * 
 * It first checks to see if the airport is cached locally in the database. If not, it retrieves the
 * information from the FlightStats service
 */
@Entity
public class Airport {
	/* Web service end point for the Airport REST API */
	private static final String flightStatsEndpoint = "	https://api.flightstats.com/flex/airports/rest/v1/json/iata/";
	private static final String appId = "5a67d947";
	private static final String appKey = "57b4712569ec1747df321829c3b46b80";
	
	private String fs; 			/*	The FlightStats code for the airport, globally unique across time.*/
	private String iata;			/* The IATA code for the airport */
	private String icao;			/* The ICAO code for the airport */
	private String faa;			/* The FAA code for the airport */
	private String name;			/* The name of the airport */
	private String street1;		/* The street address of the airport, part 1 */
	private String street2; 		/* Street address of the airport, part 2 */
	private String city;			/* The city with which the airport is associated */
	private String cityCode;		/* The city code with which the airport is associated */
	private String stateCode; 	/* State in which the airport is located */
	private String postalCode; 	/* The postal code in which the airport resides */
	private String countryCode; 	/* The code for the country in which the airport is located */
	private String countryName; 	/* The name of the country in which the Airport is located. */
	private String regionName; 	/* The name of the region in which the Airport is located */
	private String timeZoneRegionName; /* The name of the Time Zone region in which the Airport is located */
	private String weatherZone; 	/* The NOAA weather zone (US only) in which the Airport is located */
	private double latitude; 		/* The latitude of the airport in decimal degrees.*/
	private double longitude;		/* The longitude of the airport in decimal degrees.*/
	
	private Airport()
	{  }
	
	public static Airport lookup(String iataCode)
	{
		/* Check if we've got an argument */
		if ( iataCode == null )
			throw new NullPointerException();
		
		if (!(iataCode instanceof String) && (iataCode.length() ==3))
			throw new IllegalArgumentException( "Parameter must be an instance of String and be 3 characters long");
		
		/* TODO: Check if we've already retrieved the airport before and retrieve from the database */
		EntityManager em = EMF.get().createEntityManager();
		
		/* retrieve the data from the FlightStats web service */
		Airport airport = null;
		try
		{
			URL fsAirportRequest = new URL( flightStatsEndpoint + iataCode + "?appId=" + appId + "&appKey=" +appKey );
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fsAirportRequest.openStream()));
		
			JsonParser jsonParser = Json.createParser(reader);
			airport = new Airport();
			
			JsonParser.Event event = jsonParser.next();
			
			while( event != Event.END_OBJECT )
			{
				if ( event == Event.KEY_NAME ) /* we've got a key*/
				{
					switch( jsonParser.getString())
					{
					case "fs":
						event = jsonParser.next(); /* get the value */
						airport.fs = jsonParser.getString();
						break;
						
					case "iata":
						event = jsonParser.next(); /* get the value */
						airport.iata = jsonParser.getString();
						break;
						
					case "faa":
						event = jsonParser.next(); /* get the value */
						airport.faa = jsonParser.getString();
						break;
						
					case "name":
						event = jsonParser.next(); /* get the value */
						airport.name = jsonParser.getString();
						break;
						
					case "street1":
						event = jsonParser.next(); /* get the value */
						airport.street1 = jsonParser.getString();
						break;
						
					case "street2":
						event = jsonParser.next(); /* get the value */
						airport.street2 = jsonParser.getString();
						break;

					case "city":
						event = jsonParser.next(); /* get the value */
						airport.city = jsonParser.getString();
						break;
						
					case "cityCode":
						event = jsonParser.next(); /* get the value */
						airport.cityCode = jsonParser.getString();
						break;
						
					case "stateCode":
						event = jsonParser.next(); /* get the value */
						airport.stateCode = jsonParser.getString();
						break;
					
					case "postalCode":
						event = jsonParser.next(); /* get the value */
						airport.postalCode = jsonParser.getString();
						break;

					case "countryCode":
						event = jsonParser.next(); /* get the value */
						airport.countryCode = jsonParser.getString();
						break;
						
					case "countryName":
						event = jsonParser.next(); /* get the value */
						airport.countryName = jsonParser.getString();
						break;
						
					case "regionName":
						event = jsonParser.next(); /* get the value */
						airport.regionName = jsonParser.getString();
						break;
						
					case "timeZoneRegionName":
						event = jsonParser.next(); /* get the value */
						airport.timeZoneRegionName = jsonParser.getString();
						break;
						
					case "weatherZone":
						event = jsonParser.next(); /* get the value */
						airport.weatherZone = jsonParser.getString();
						break;
						
					case "latitude":
						event = jsonParser.next(); /* get the value */
						airport.latitude = Double.parseDouble(jsonParser.getString());
						break;
						
					case "longitude":
						event = jsonParser.next(); /* get the value */
						airport.longitude = Double.parseDouble(jsonParser.getString());
						break;

					}
				}
				event=jsonParser.next();
			}
			jsonParser.close();
		}
		catch (MalformedURLException e)
		{
			System.out.println("MalformedURLException in Airport.lookup()");
		}
		catch (IOException e)
		{
			System.out.println("IOException in Airport.lookup()");
		}
		finally
		{
			em.close();
		}

		return airport;
	}


	public String getFs() {
		return fs;
	}

	public String getIata() {
		return iata;
	}

	public String getIcao() {
		return icao;
	}

	public String getFaa() {
		return faa;
	}

	public String getName() {
		return name;
	}

	public String getStreet1() {
		return street1;
	}

	public String getStreet2() {
		return street2;
	}

	public String getCity() {
		return city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getTimeZoneRegionName() {
		return timeZoneRegionName;
	}

	public String getWeatherZone() {
		return weatherZone;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
