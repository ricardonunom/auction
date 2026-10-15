package com.auctionhouse.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal value, Currency currency) implements Comparable<Money> {
    
    public Money {
        if (value == null || currency == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new MoneyException("Valor e moeda não podem ser nulos nem negativos");
        }
        value = value.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
    }

    public Money add(Money other) {
        requireSameCurrency(other);

        return new Money(this.value.add(other.value), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return this.value.compareTo(other.value);
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new MoneyException("Moedas diferentes: " + this.currency + " vs " + other.currency);
        }
    }
}
