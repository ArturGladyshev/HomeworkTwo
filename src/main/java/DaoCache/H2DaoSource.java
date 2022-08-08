package DaoCache;

import org.h2.jdbc.JdbcSQLNonTransientException;
import org.h2.tools.Server;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*Класс для работы с базой данных. В базе хранится уникальное имя класса и бинарное
 представление возвращаемого значения, а также таблица числ Фибоначчи. База данных описана в schema.sql.
*/

public class H2DaoSource implements Source
{
		static final String FIND_FIBONACCI_NUMBERS = "select * from fibonacci f where f.number<=?";

		static final String FIND_MAX_FIBONACCI_NUMBER = "select max(number) from fibonacci";

		static final String INSERT_FIBONACCI_NUMBER = "insert into fibonacci (number) values (?)";

		static final String GET_FIBONACCI_VALUE_BY_ID = "select * from fibonacci f where f.fibonacci_id=?";

		static final String INSERT_METHOD_INFO = "insert into cache (method_key, value) values (?, ?)";

		static final String GET_METHOD_INFO_BY_KEY = "select * from cache c where c.method_key=?";

		static final String GET_METHOD_INFO_BY_KEY_AND_VALUE = "select * from cache c where c.method_key=? and c.value=?";

		static final String GET_CACHE_VALUES = "select * from cache";

		private String url;

		private String name;

		private String password;

		public H2DaoSource(String url, String name, String password)
		{
				this.url = url;
				this.name = name;
				this.password = password;
		}

		@Override
		public String getUrl()
		{
				return url;
		}


		@Override
		public void setUrl(String url)
		{
				this.url = url;
		}


		@Override
		public String getName()
		{
				return name;
		}


		@Override
		public void setName(String name)
		{
				this.name = name;
		}


		@Override
		public String getPassword()
		{
				return password;
		}


		@Override
		public void setPassword(String password)
		{
				this.password = password;
		}


		public static Connection connection(H2DaoSource source) throws SQLException
		{
				final Connection connection = DriverManager.getConnection(source.getUrl(), source.getName(), source.getPassword());
				connection.setAutoCommit(true);
				return connection;
		}

		//Создание базы данных
		@Override
		public void createDatabase()
		{
				StringBuilder sqlBuilder = new StringBuilder();
				try
				{
						BufferedReader reader = new BufferedReader(new FileReader("src\\main\\resources\\schema.sql"));
						reader.lines().forEach(line ->sqlBuilder.append(line + "\n"));
				}
				catch(IOException e)
				{
						throw new RuntimeException(e);
				}
				try (PreparedStatement statement = H2DaoSource.connection(this)
					.prepareStatement(sqlBuilder.toString()))
				{
						statement.execute();
						Server.createTcpServer().start();
				}
				catch(SQLException e)
				{
						throw new RuntimeException(e);
				}
		}

		/*Поиск значения по уникальной строке из имени метода и результата его работы
		 с конкретными значениями параметров */
		@Override
		public Object findMethodResult(String methodKey) throws NoFoundValueException
		{
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(GET_METHOD_INFO_BY_KEY))
				{
						statement.setString(1, methodKey);
						statement.execute();
						ResultSet resultSet = statement.getResultSet();
						resultSet.next();
						return getResult(resultSet);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				return null;
		}

		/*Поиск результата метода по ключу и байтовому массиву, когда байтовое
		 представление результата уже известно
		 */
		public Object findBinaryValue(String nameMethod, byte[] bytes) throws NoFoundValueException
		{
				Object value = null;
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(GET_METHOD_INFO_BY_KEY_AND_VALUE))
				{
						statement.setString(1, nameMethod);
						statement.setBytes(2, bytes);
						statement.execute();
						ResultSet resultSet = statement.getResultSet();
						resultSet.next();
						value = getResult(resultSet);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				return value;
		}

		//Добавление ключа и результата работы метода в таблицу БД
		@Override
		public Object addMethodInfo(String methodName, Object value) throws NoFoundValueException
		{
				Object object = null;
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(INSERT_METHOD_INFO))
				{
						ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
						ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
						objectStream.writeObject(value);
						byte[] binaryValue = byteStream.toByteArray();
						byteStream.close();
						objectStream.close();
						statement.setString(1, methodName);
						statement.setBytes(2, binaryValue);
						statement.execute();
						object = findBinaryValue(methodName, binaryValue);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				catch(IOException e)
				{
						e.printStackTrace();
				}
				return object;
		}

		//Обработка результата
		private Object getResult(ResultSet resultSet) throws NoFoundValueException
		{
				Object value = null;
				try
				{
						if(resultSet.wasNull())
						{
								throw new NoFoundValueException("Не найдено имя метода");
						}
						byte[] bytes = resultSet.getBytes(3);
						ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
						ObjectInputStream objectStream = new ObjectInputStream(byteStream);
						value = objectStream.readObject();
				}
				catch(IOException e)
				{
						e.printStackTrace();
				}
				catch(JdbcSQLNonTransientException ej)
				{
						throw new NoFoundValueException("Не найдено имя метода");
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				catch(ClassNotFoundException classNotFoundException)
				{
						classNotFoundException.printStackTrace();
				}
				catch(Exception io)
				{
						io.printStackTrace();
				}
				return value;
		}

		//Вывод значений базы данных
		@Override
		public Set<Object> getCacheValues()
		{
				HashSet<Object> values;
				try (Statement statement = H2DaoSource.connection(this).createStatement())
				{
						statement.execute(GET_CACHE_VALUES);
						ResultSet resultSet = statement.getResultSet();
						values = new HashSet<>();
						while(resultSet.next())
						{
								Object value = getResult(resultSet);
								values.add(value);
						}
				}
				catch(SQLException e)
				{
						throw new RuntimeException(e);
				}
				return values;
		}

		//Поиск листа Фибоначчи в базе
		public List<Long> findFibonacciNumbers(Long id) throws NoFoundValueException
		{
				List<Long> numbers = new ArrayList<>();
				Long targetNumber = null;
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(GET_FIBONACCI_VALUE_BY_ID))
				{
						statement.setLong(1, id);
						statement.execute();
						ResultSet resultSet = statement.getResultSet();
						resultSet.next();
						targetNumber = getFibonacciTarget(resultSet, 2);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(FIND_FIBONACCI_NUMBERS))
				{
						statement.setLong(1, targetNumber);
						statement.execute();
						ResultSet resultSet = statement.getResultSet();
						numbers = getFibonacciNumbers(resultSet);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				return numbers;
		}

		//Обработка результата и поиск целевого значения
		private Long getFibonacciTarget(ResultSet resultSet, int column) throws NoFoundValueException
		{
				Long result;
				try
				{
						result = resultSet.getLong(column);
						if(resultSet.wasNull())
						{
								throw new NoFoundValueException("Нет значения");
						}
				}
				catch(SQLException throwable)
				{
						throw new NoFoundValueException("Нет значения");
				}
				return result;
		}

		//Добавление первого значения, если таблица пуста
		private Long getMax(ResultSet resultSet, int column) throws TableEmptyException
		{
				Long fibonacci = null;
				try
				{
						fibonacci = resultSet.getLong(column);
						if(resultSet.wasNull())
						{
								throw new TableEmptyException();
						}
				}
				catch(TableEmptyException tableEmptyException)
				{
						try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement
							("insert into fibonacci (number) values(0)"))
						{
						statement.execute();
						}
						catch(SQLException sqlException)
						{
								sqlException.printStackTrace();
						}
				}
				catch(SQLException sqlException)
				{
						throw new NoFoundValueException("Нет значения");
				}
				return fibonacci;
		}

		//Добавление числа и всех предшествующих ему, если их не было в базе
		@Override
		public List<Long> addFibonacciNumbers(Long index, List<Long> fibonacciNumbers)
		{
				Long max = null;
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(FIND_MAX_FIBONACCI_NUMBER))
				{
						statement.execute();
						ResultSet resultSet = statement.getResultSet();
						resultSet.next();
						max = getMax(resultSet, 1);
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				try (PreparedStatement statement = H2DaoSource.connection(this).prepareStatement(INSERT_FIBONACCI_NUMBER))
				{
						for(int i = fibonacciNumbers.indexOf(max); i < fibonacciNumbers.size(); ++i)
						{
								statement.setLong(1, fibonacciNumbers.get(i));
								statement.addBatch();
						}
						statement.executeBatch();
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				return fibonacciNumbers;
		}

		//Добавление результатов возвращаемого значения в лист и их возвращение
		private List<Long> getFibonacciNumbers(ResultSet resultSet) throws NoFoundValueException
		{
				List<Long> fibonacciList = new ArrayList<>();
				try
				{
						while(resultSet.next())
						{
								long number = resultSet.getLong(2);
								fibonacciList.add(number);
						}
				}
				catch(SQLException throwable)
				{
						throwable.printStackTrace();
				}
				return fibonacciList;
		}
}