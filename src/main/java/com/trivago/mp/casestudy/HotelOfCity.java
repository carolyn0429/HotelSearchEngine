package com.trivago.mp.casestudy;

/**
 * Created by carolynhung on 3/22/18.
 */
public class HotelOfCity extends Hotel{

    private final int cityId;

    public HotelOfCity(int id, int cityId, String name, int rating, int stars) {
        super(id, name, rating, stars);
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }
}
