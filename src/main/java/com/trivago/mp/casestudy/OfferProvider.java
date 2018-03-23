package com.trivago.mp.casestudy;

import java.util.List;
import java.util.Map;

/**
 * This is used to retrieve offered prices from advertisers on trivago.
 */
public interface OfferProvider {

    /**
     * Retrieve hotel stay prices from trivago advertisers. This is generally a very expensive operation (network
     * requests, database queries, etc.) and so should be called sparingly.
     *
     * @param advertiser the advertiser to query for prices
     * @param hotelIds   IDs of hotels that prices are needed for
     * @param dateRange  stay dates that prices are needed for
     * @return a map {hotelID -> AdvertiserOffer} containing the advertiser's price for each requested hotel
     */
    Map<Integer, Offer> getOffersFromAdvertiser(Advertiser advertiser, List<Integer> hotelIds, DateRange dateRange);
}
