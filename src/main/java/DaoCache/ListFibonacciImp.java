package DaoCache;

import java.util.ArrayList;
import java.util.List;

//Метод для поиска чисел Фибоначчи
public class ListFibonacciImp implements FibonacciSequenceFinder
{
		@Override
		public long fibonacci(long n)
		{
				if(n <= 1l)
						return n;
				else
						return fibonacci(n - 1l) + fibonacci(n - 2l);
		}

		@Override
		public List<Long> findFibonacciSequence(long n)
		{
				List<Long> list = new ArrayList<>();
				for(long i = 0; i < n; i++)
				{
						long f = fibonacci(i);
						if(f < 0 || f >= Long.MAX_VALUE)
						{
								throw new IllegalArgumentException();
						}
						list.add(f);
				}
				return list;
		}
}