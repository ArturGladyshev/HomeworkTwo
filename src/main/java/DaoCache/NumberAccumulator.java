package DaoCache;

public interface NumberAccumulator
{
    @Caching(source = H2DaoSource.class)
    int sumOfNumbers(int one, int two);

    @Caching(source = H2DaoSource.class)
    int sumOfNumbers(int one, double two);

    @Caching(source = H2DaoSource.class)
    default Integer printNull()
    {
        Integer n = null;
        return n;
    }
}
