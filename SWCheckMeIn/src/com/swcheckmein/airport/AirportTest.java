package com.swcheckmein.airport;

public class AirportTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Airport airport = Airport.lookup("PDX");
		System.out.println("fs = " + airport.getFs());
		System.out.println("iata = " + airport.getIata());	
		System.out.println("icao = " + airport.getIcao());
		System.out.println("faa = " + airport.getFaa());
		System.out.println( "name = " + airport.getName());
		System.out.println( "street1 = " + airport.getStreet1());
		System.out.println( "street2 = " + airport.getStreet2());
		System.out.println( "city = " + airport.getCity());
		System.out.println( "cityCode = " + airport.getCityCode());
		System.out.println( "stateCode = " + airport.getStateCode());
		System.out.println( "postalCode = " + airport.getPostalCode());
		System.out.println( "countryCode = " + airport.getCountryCode());
		System.out.println( "countryName = " + airport.getCountryName());
		System.out.println( "regionName = " + airport.getRegionName());
		System.out.println( "timeZoneRegionName = " + airport.getTimeZoneRegionName());
		System.out.println( "weatherZone = " + airport.getWeatherZone());
		System.out.println( "latitude = " + String.valueOf( airport.getLatitude()));
		System.out.println( "longitude =" + String.valueOf( airport.getLongitude() ));
	}

}
