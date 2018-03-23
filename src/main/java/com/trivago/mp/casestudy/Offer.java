package com.trivago.mp.casestudy;

/**
 * A concrete offer from a specific advertiser
 */
public class Offer {
    private final Advertiser advertiser;
    private final int priceInEuro;
    private final int cpc;

    public Offer(Advertiser advertiser, int priceInEuro, int cpc) {
        this.advertiser = advertiser;
        this.priceInEuro = priceInEuro;
        this.cpc = cpc;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public int getPriceInEuro() {
        return priceInEuro;
    }

    /**
     * the cost per click an advertiser pays for a particular offer
     */
    public int getCpc() {
        return cpc;
    }

    @Override
    public String toString() {
        return "Offer{" + "advertiser=" + advertiser + ", priceInEuro=" + priceInEuro + ", cpc=" + cpc + '}';
    }
}
