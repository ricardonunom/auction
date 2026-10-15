package com.auctionhouse.domain;

import java.time.Instant;

public record Bid(Money amount, java.util.UUID bidderId, Instant placedAt) {
        public Bid {
            if (amount == null || bidderId == null || bidderId.toString().isEmpty()) {
                throw new IllegalArgumentException("Amount and bidderId cannot be null or empty");
            }
        }

}
