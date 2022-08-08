package DaoCache;

import java.util.List;

public interface FibonacciSequenceFinder
{
		long fibonacci(long target);

		@Caching(source = H2DaoSource.class, searchFibonacciSequence = true)
		List<Long> findFibonacciSequence(long target);
}
