package DaoCache;

import java.util.List;
import java.util.Set;

//Интерфейс для классов, работающих с кэшем данных
public interface Source
{
		Object findMethodResult(String methodKey);

		Object addMethodInfo(String methodName, Object value);

		List<Long> findFibonacciNumbers(Long number);

		List<Long> addFibonacciNumbers(Long number, List<Long> fibonacciNumbers);

		void createDatabase();

		Set<Object> getCacheValues();

		String getUrl();

		void setUrl(String url);

		String getName();

		void setName(String name);

		String getPassword();

		void setPassword(String password);


}
