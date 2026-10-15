public class BidTest {
 @Test
    public void testBidCreation() {
        Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("USD"));
        Bid bid = new Bid(java.util.UUID.randomUUID(), amount, Instant.now());
        assertNotNull(bid);
        assertEquals(amount, bid.getAmount());
    }   
}
