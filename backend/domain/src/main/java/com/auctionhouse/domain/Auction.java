package com.auctionhouse.domain;

import java.time.Instant;

public class Auction {
    private java.util.UUID id;
    private String title;
    private Money startingPrice;
    private Instant endsAt;
    private AuctionState status;
    private Bid currentBid;

    public Auction(String title, Money startingPrice) {
        this.id = java.util.UUID.randomUUID();
        this.title = title;
        this.startingPrice = startingPrice;
        this.status = AuctionState.CREATED;
    }

    public boolean close(Instant now) {
        this.status = AuctionState.ENDED;
        this.endsAt = now;
        return true;
    }

    public boolean open(String title,Instant now) {
        this.status = AuctionState.ACTIVE;
        this.title = title;
        this.endsAt = now;
        return true;
    }

    public void placeBid(java.util.UUID bidderId, Money amount, Instant now) {
        if(this.status != AuctionState.ACTIVE) {
            throw new AuctionException("Auction is not active");
        }

        if(now.isAfter(endsAt)) {
            throw new AuctionException("Auction has ended");
        }

        if(currentBid != null && (amount.isGreaterThan(this.startingPrice) || amount.isGreaterThan(this.currentBid.amount()))) {
            throw new AuctionException("Bid amount is less than starting price");
        }

        if(bidderId != null && bidderId.equals(this.currentBid.bidderId())) {
            throw new AuctionException("You are already the highest bidder");
        }

        this.currentBid = new Bid(amount, bidderId, now);
    }
    
}
