package DaoCache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

//Класс кэширует данные исходя из логики интерфейса Source
public class ProxyCache implements InvocationHandler
{
		private Object delegate;

		private Source source;

		private String methodName = null;

		public ProxyCache(Object delegate)
		{
				this.delegate = delegate;
		}

		protected static <T> T getProxyInstance(Object delegate)
		{
				return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), delegate.getClass().getInterfaces(),
					new ProxyCache(delegate));
		}

		/* Проверка класса и установка настроек при первом вызове метода Invoke или при смене состояния объекта source.
		 Условие задачи требует хранить Source как интерфейс, потому для инициализации необходимо уточнять конкретный класс.
		 Если использовать абстрактный класс вместо интерфейса, то можно ограничиться вызовом конструктора
		 через рефлексию.
		 */
		public void setProperty(Caching cachingAnnotation)

		{
				if(cachingAnnotation.source().getName().equals("DaoCache.H2DaoSource"))
				{
						if(this.source == null)
						{
								H2DaoSource h2DaoSource = new H2DaoSource(cachingAnnotation.url(), cachingAnnotation.user(), cachingAnnotation.password());
								h2DaoSource.createDatabase();
								this.source = h2DaoSource;
						}
						else
						{
								if(this.source.getUrl() != cachingAnnotation.url() || this.source.getName() != cachingAnnotation.url())
								{
										this.source.setName(cachingAnnotation.user());
										this.source.setUrl(cachingAnnotation.url());
										this.source.setPassword(cachingAnnotation.password());
										this.source.createDatabase();
								}
						}
				}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		{
				Object value = null;
				try
				{
						if(!method.isAnnotationPresent(Caching.class))
						{
								System.out.println("Метод без аннотации Cache");
								return method.invoke(delegate, args);
						}
						Caching cacheAnnotation = method.getAnnotation(Caching.class);
						if(methodName == null || !methodName.equals(method.getName()))
						{
								this.setProperty(cacheAnnotation);
								methodName = method.getName();
						}
						if(cacheAnnotation.searchFibonacciSequence() == true)
						{
								try
								{
										value = source.findFibonacciNumbers((Long)args[0]);
										System.out.println("Вернулось значение из кэша" + value);
										return value;
								}
								catch(NoFoundValueException em)
								{
										value = source.addFibonacciNumbers((Long)args[0], (List<Long>)method.invoke(delegate, args));
										System.out.println("Добавлено значение в кэш " + value);
										return value;
								}
						}
						MethodInfo methodInfo = new MethodInfo(method, args);
						try
						{
								value = source.findMethodResult(methodInfo.toString());
						}
						catch(NoFoundValueException em)
						{
								value = method.invoke(delegate, args);
								return source.addMethodInfo(methodInfo.toString(), value);
						}
				}
				catch(IllegalAccessException e)
				{
						e.printStackTrace();
				}
				catch(InvocationTargetException e)
				{
						e.printStackTrace();
				}
				return value;
		}
}