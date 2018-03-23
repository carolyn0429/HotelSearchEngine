package com.trivago.mp.casestudy;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: Implement this class.
 * Your task will be to implement two functions, one for loading the data which is stored as .csv files in the ./data
 * folder and one for performing the actual search.
 */
public class HotelSearchEngineImpl implements HotelSearchEngine {
    private List<HotelOfCity> hotels = new ArrayList<>();
    private List<Advertiser> advertisers = new ArrayList<>();
    private HashMap<String, Integer> cityMap = new HashMap<>();
    private HashMap<Integer, List<Integer>> advertiseHotelMap = new HashMap<>();
    private HashMap<Integer, List<Integer>> cityHotelMap = new HashMap<>();

    @Override
    public void initialize() {

        hotelsCsvReader();
        advertiserCsvReader();
        citiesCsvReader();
        advertiseHotelMapCsvReader();
        cityHotelMapCsvReader();

    }

    private void cityHotelMapCsvReader() {

        for(int i=0; i<hotels.size(); i++){
            if (cityHotelMap.containsKey(hotels.get(i).getCityId())){
                List<Integer> currentHotelList = cityHotelMap.get(hotels.get(i).getCityId());
                currentHotelList.add(i);
                List<Integer> newHotelList = new ArrayList<>(currentHotelList);
                cityHotelMap.put(hotels.get(i).getCityId(), newHotelList);
            } else {
                cityHotelMap.put(hotels.get(i).getCityId(), Arrays.asList(i));
            }
        }

    }

    private void advertiseHotelMapCsvReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("data/hotel_advertiser.csv"));
            String line = "";
            reader.readLine();
            while((line = reader.readLine()) != null){
                String[] dataList = line.split(",");
                if(dataList.length > 0) {
                    if (advertiseHotelMap.containsKey(Integer.valueOf(dataList[0]))){
                        List<Integer> currentHotelList = advertiseHotelMap.get(Integer.valueOf(dataList[0]));
                        currentHotelList.add(Integer.valueOf(dataList[1]));
                        List<Integer> newHotelList = new ArrayList<>(currentHotelList);
                        advertiseHotelMap.put(Integer.valueOf(dataList[0]), newHotelList);
                    } else {
                        advertiseHotelMap.put(Integer.valueOf(dataList[0]), Arrays.asList(Integer.valueOf(dataList[1])));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public List<HotelWithOffers> performSearch(String cityName, DateRange dateRange, OfferProvider offerProvider) {

        List<HotelWithOffers> hotelsWithOffers = new ArrayList<>();
        HashMap<Integer, Integer> updatedAdvHotelMap = updateAdvertiseHotelMapByCity(cityName);
        for (int i=0; i<advertisers.size(); i++) {
            if (advertiseHotelMap.get(i).size() > 0){
                //offerProvider.getOffersFromAdvertiser(advertisers.get(i), ,dateRange)
            }
        }

        return hotelsWithOffers;
    }

    private HashMap<Integer, Integer> updateAdvertiseHotelMapByCity(String cityName) {
        HashMap<Integer, Integer> result = new HashMap<>();
        int cityId = cityMap.get(cityName);
        List<Integer> hotelIds = cityHotelMap.get(cityId);
        for (int i=0; i<advertiseHotelMap.size(); i++){
           // if (advertiseHotelMap.get(i)
        }

        return result;
    }
}
