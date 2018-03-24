package com.trivago.mp.casestudy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Author: Carolyn Hung 3/22/2018
 * Your task will be to implement two functions, one for loading the data which is stored as .csv files in the ./data
 * folder and one for performing the actual search.
 *
 * ====Note====
 * Improvement: perform ranking based on rating, city name, with given hotel stars range.
 * Improvement: perform customer interest rate based on clicks, impression with given date range and city name.
 */
public class HotelSearchEngineImpl implements HotelSearchEngine {
    private List<HotelOfCity> hotels = new ArrayList<>();
    private List<Advertiser> advertisers = new ArrayList<>();
    private Map<String, Integer> cityMap = new HashMap<>();
    private Map<Integer, List<Integer>> advertiseHotelMap = new HashMap<>();
    private Map<Integer, List<Integer>> hotelAdvertiseMap = new HashMap<>();
    private Map<Integer, List<Integer>> cityHotelMap = new HashMap<>();

    @Override
    public void initialize() {
        hotelsCsvReader();
        advertiserCsvReader();
        citiesCsvReader();
        advertiseHotelMapCsvReader();
        hotelAdvertiseMapCsvReader();
        cityHotelMapCsvReader();
    }

    /**
     * create hotel_advertise map
     */
    private void hotelAdvertiseMapCsvReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/hotel_advertiser.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] dataList = line.split(",");
                if(dataList.length > 0) {
                    if (hotelAdvertiseMap.containsKey(Integer.valueOf(dataList[1]))){
                        List<Integer> currentAdvertiseList = hotelAdvertiseMap.get(Integer.valueOf(dataList[1]));
                        currentAdvertiseList.add(Integer.valueOf(dataList[0]));
                        List<Integer> newAdvertiseList = new ArrayList<>(currentAdvertiseList);
                        hotelAdvertiseMap.put(Integer.valueOf(dataList[1]), newAdvertiseList);
                    } else {
                        hotelAdvertiseMap.put(Integer.valueOf(dataList[1]), new ArrayList<>(Arrays.asList(Integer.valueOf(dataList[0]))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create city_hotel map
     */
    private void cityHotelMapCsvReader() {

        for(int i=0; i<hotels.size(); i++){
            if (cityHotelMap.containsKey(hotels.get(i).getCityId())){
                List<Integer> currentHotelList = cityHotelMap.get(hotels.get(i).getCityId());
                currentHotelList.add(i);
                List<Integer> newHotelList = new ArrayList<>(currentHotelList);
                cityHotelMap.put(hotels.get(i).getCityId(), newHotelList);
            } else {
                cityHotelMap.put(hotels.get(i).getCityId(), new ArrayList<>(Arrays.asList(i)));
            }
        }

    }

    /**
     * create advertise_hotel map
     */
    private void advertiseHotelMapCsvReader() {
        BufferedReader reader;
        try {
            // advertiser id, hotel id
            reader = new BufferedReader(new FileReader("data/hotel_advertiser.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] dataList = line.split(",");
                if(dataList.length > 0) {
                    if (advertiseHotelMap.containsKey(Integer.valueOf(dataList[0]))){
                        List<Integer> currentHotelList = advertiseHotelMap.get(Integer.valueOf(dataList[0]));
                        currentHotelList.add(new Integer(Integer.valueOf(dataList[1])));
                        List<Integer> newHotelList = new ArrayList<>(currentHotelList);
                        advertiseHotelMap.put(Integer.valueOf(dataList[0]), newHotelList);
                    } else {
                        advertiseHotelMap.put(Integer.valueOf(dataList[0]), new ArrayList<>(Arrays.asList(Integer.parseInt(dataList[1]))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create cities list
     */
    private void citiesCsvReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/cities.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] cityList = line.split(",");
                if(cityList.length > 0) {
                    cityMap.put(cityList[1], Integer.valueOf(cityList[0]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create advertisers list
     */
    private void advertiserCsvReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/advertisers.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] adList = line.split(",");
                if(adList.length > 0) {
                    Advertiser ad = new Advertiser(Integer.parseInt(adList[0]), adList[1]);
                    advertisers.add(ad);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create hotels list
     */
    private void hotelsCsvReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/hotels.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] hotelList = line.split(",");
                if(hotelList.length > 0) {
                    HotelOfCity hotel = new HotelOfCity(Integer.parseInt(hotelList[0]),
                            Integer.parseInt(hotelList[1]),
                            hotelList[4],Integer.parseInt(hotelList[5]),
                            Integer.parseInt(hotelList[6]));
                    hotels.add(hotel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param cityName given city name
     * @param dateRange given date range
     * @param offerProvider offer provider interface to call finding offer per advertiser
     * @return list of hotel with offers
     */
    @Override
    public List<HotelWithOffers> performSearch(String cityName, DateRange dateRange, OfferProvider offerProvider) {

        List<HotelWithOffers> hotelsWithOffers = new ArrayList<>();

        // update advertise_hotel map based on city name
        Map<Integer, List<Integer>> updatedAdvertiseHotelMap = updateAdvertiseHotelMap(cityName);

        // perform search with filtered advertise_hotel map to reduce expensive call times.
        for (Map.Entry<Integer, List<Integer>> entry : updatedAdvertiseHotelMap.entrySet()){
            Integer key = entry.getKey();
            List<Integer> hotelIds = entry.getValue();

            // Id is hotel id,
            Map<Integer, Offer> offers = offerProvider.getOffersFromAdvertiser(advertisers.get(key), hotelIds, dateRange);

            // convert offers to hotelWithOffers
            for (Map.Entry<Integer, Offer> offerEntry : offers.entrySet()){
                Integer hotelId = offerEntry.getKey();
                Offer offer = offerEntry.getValue();

                // if hotel is existed in offer list, take list out and add offer into existing offer list
                if (findHotelWithOffersInHotelsWithOffers(hotelsWithOffers, hotelId) != null){
                    HotelWithOffers existingHotelWithOffers = findHotelWithOffersInHotelsWithOffers(hotelsWithOffers, hotelId);
                    int index = hotelsWithOffers.indexOf(existingHotelWithOffers);
                    List<Offer> existingOffers = new ArrayList<>(hotelsWithOffers.get(index).getOffers());
                    existingOffers.add(offer);

                } else {
                    // initialize new HotelWithOffers for this hotelId.
                    HotelWithOffers hotelWithOffers = new HotelWithOffers(hotels.get(hotelId));
                    // add initial offer as list
                    hotelWithOffers.setOffers(Arrays.asList(offer));
                    hotelsWithOffers.add(hotelWithOffers);
                }
            }
        }
        return hotelsWithOffers;
    }

    /**
     * find hotel with offers within existing list of hotels with offers
     * @param hotelsWithOffers list of existing hotels with offers
     * @param hotelId hotel id
     * @return HotelWithOffers hotel offers with given hotelId
     */
    private HotelWithOffers findHotelWithOffersInHotelsWithOffers(List<HotelWithOffers> hotelsWithOffers, int hotelId) {

        return hotelsWithOffers.stream().filter(hotelWithOffers -> (hotelId == hotelWithOffers.getHotel().getId()))
                .findFirst().orElse(null);
    }

    /**
     * update advertise hotel map filtered by given city name
     * @param cityName city name
     * @return updated advertise_hotel map
     */
    private Map<Integer, List<Integer>> updateAdvertiseHotelMap(String cityName) {
        Map<Integer, List<Integer>> updatedHotelAdvertiseMap = updateHotelAdvertiseMapByCity(cityName);
        Map<Integer, List<Integer>> updatedAdvertiseHotelMap = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : updatedHotelAdvertiseMap.entrySet()){
            Integer hotelId = entry.getKey();
            List<Integer> listOfAdvertisers = entry.getValue();
            for(int i=0; i<listOfAdvertisers.size(); i++){
                if (updatedAdvertiseHotelMap.containsKey(listOfAdvertisers.get(i))){
                    List<Integer> currentAdvList = updatedAdvertiseHotelMap.get(listOfAdvertisers.get(i));
                    currentAdvList.add(hotelId);
                    updatedAdvertiseHotelMap.put(listOfAdvertisers.get(i), currentAdvList);
                } else {
                    updatedAdvertiseHotelMap.put(listOfAdvertisers.get(i), new ArrayList<>(Arrays.asList(hotelId)));
                }
            }
        }
        return updatedAdvertiseHotelMap;
    }

    /**
     * update hotel_advertise map filtered by given city name
     * @param cityName city name
     * @return updated hotel_advertise map
     */
    private HashMap<Integer, List<Integer>> updateHotelAdvertiseMapByCity(String cityName) {

        int cityId = cityMap.get(cityName);
        List<Integer> hotelIds = cityHotelMap.get(cityId);
        HashMap<Integer, List<Integer>> updatedHotelAdvertiseMap = new HashMap<>();
        for (int i=0; i<hotelIds.size(); i++){
          updatedHotelAdvertiseMap.put(hotelIds.get(i), hotelAdvertiseMap.get(hotelIds.get(i)));
        }
        return updatedHotelAdvertiseMap;
    }
}
