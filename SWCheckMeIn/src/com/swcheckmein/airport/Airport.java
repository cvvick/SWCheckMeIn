package com.swcheckmein.airport;

import java.lang.NullPointerException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

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
		
		/* Check to see if we've already retrieved this airport from FlightStats */
		EntityManager em = EMF.get().createEntityManager();
		
		List<CachedAirport> result = em.createQuery( 
                "SELECT e " + 
                "FROM CachedAirport e "+ 
                "WHERE e.name = ?1 ",  CachedAirport.class) 
          .setParameter(1, iataCode) 
          .getResultList(); 
		
		int count = 0; 
		for (CachedAirport e : result) { 
			System.out.println(++count + ": " + e.getIata() + ", " + e.getName()); 
		} 
		
		/* TODO: Copy the CachedAirport to a new Airport instance */
		
		/* retrieve the data from the FlightStats web service */
		Airport airport = null;
		try
		{
			URL fsAirportRequest = new URL( flightStatsEndpoint + iataCode + "?appId=" + appId + "&appKey=" +appKey );
						
			URLConnection connection = fsAirportRequest.openConnection();
			/* connection.setConnectTimeout(0); */
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
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
			
			/* Store the airport information retrieved from FlightStats */
			em.getTransaction().begin();
			
			CachedAirport cachedAirport = CachedAirport.valueOf( airport );
			em.persist(cachedAirport);
			em.getTransaction().commit();		
		}
		catch (MalformedURLException e)
		{
			System.out.println("MalformedURLException in Airport.lookup(): " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IOException in Airport.lookup(): " + e.getMessage());
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

/*
 * Cached airport entity that represents a airport that has been previously retrieved from Flightstats
 */
@Entity
class CachedAirport
{
	private String fs; 			/*	The FlightStats code for the airport, globally unique across time.*/
	@Id
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
	
	public CachedAirport()
	{  }
	
	public static CachedAirport valueOf(Airport airport)
	{
		CachedAirport cachedAirport = new CachedAirport();
		
		/* defensive copy */
		cachedAirport.setFs(new String(airport.getFs()));
		cachedAirport.setIata(new String(airport.getIata()));
		cachedAirport.setIcao(new String(airport.getIcao()));
		cachedAirport.setFaa(new String(airport.getFaa()));
		cachedAirport.setName(new String(airport.getName()));
		cachedAirport.setStreet1(new String(airport.getStreet1()));
		cachedAirport.setStreet2(new String(airport.getStreet2()));
		cachedAirport.setCity(new String(airport.getCity()));
		cachedAirport.setCityCode(new String(airport.getCityCode()));
		cachedAirport.setStateCode(new String(airport.getStateCode()));
		cachedAirport.setPostalCode(new String(airport.getPostalCode()));
		cachedAirport.setCountryCode(new String(airport.getCountryCode()));
		cachedAirport.setCountryName(new String(airport.getCountryName()));
		cachedAirport.setRegionName(new String(airport.getRegionName()));
		cachedAirport.setTimeZoneRegionName(new String(airport.getTimeZoneRegionName()));
		cachedAirport.setWeatherZone(new String(airport.getWeatherZone()));
		cachedAirport.setLatitude(airport.getLatitude());
		cachedAirport.setLongitude(airport.getLongitude());
		return cachedAirport;
	}
	
	public void setFs(String fs) {
		this.fs = fs;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public void setFaa(String faa) {
		this.faa = faa;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setTimeZoneRegionName(String timeZoneRegionName) {
		this.timeZoneRegionName = timeZoneRegionName;
	}

	public void setWeatherZone(String weatherZone) {
		this.weatherZone = weatherZone;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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