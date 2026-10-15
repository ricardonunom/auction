public class AuctionTest {
    @Test
    public void testAuctionCreation() {
        Money startingPrice = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));
        Auction auction = new Auction("Test Auction", startingPrice);
        assertNotNull(auction);
        assertEquals("Test Auction", auction.getTitle());
        assertEquals(startingPrice, auction.getStartingPrice());
        assertEquals(AuctionState.CREATED, auction.getStatus());
    }
}
