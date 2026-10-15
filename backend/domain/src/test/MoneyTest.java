public class MoneyTest {
    @Test
    public void testAdd() {
        Money money1 = new Money(new BigDecimal("10.00"), Currency.getInstance("USD"));
        Money money2 = new Money(new BigDecimal("5.00"), Currency.getInstance("USD"));
        Money result = money1.add(money2);
        assertThat(result.valor()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertEquals(Currency.getInstance("USD"), result.moeda());
    }

    @Test
    public void testAddDifferentCurrency() {
        Money money1 = new Money(new BigDecimal("10.00"), Currency.getInstance("USD"));
        Money money2 = new Money(new BigDecimal("5.00"), Currency.getInstance("EUR"));
        assertThrows(MoneyException.class, () -> {
            money1.add(money2);
        });
    }
}
