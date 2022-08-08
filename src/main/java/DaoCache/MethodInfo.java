package DaoCache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodInfo implements Serializable
{
		private boolean[] usedArguments;

		private final Method method;

		private List<Object> arguments;

		public Method getMethod()
		{
				return method;
		}

		public List<Object> getArguments()
		{
				return arguments;
		}

		public boolean[] getUsedArguments()
		{
				return usedArguments;
		}

		public MethodInfo(Method method, Object[] arguments)
		{
				this.method = method;
				if(arguments != null)
				{
						this.arguments = Arrays.asList(arguments);
						usedArguments = new boolean[arguments.length];
						for(int i = 0; i < usedArguments.length; ++i)
						{
								usedArguments[i] = true;
						}
				}
		}

		@Override
		public boolean equals(Object methodsInfo)
		{
				if(this == methodsInfo)
						return true;
				if(methodsInfo == null)
						return false;
				if(methodsInfo instanceof MethodInfo)
				{
						String aKey = this.toString();
						String bKey = methodsInfo.toString();
						if(aKey.equals(bKey))
						{
								System.out.println(aKey + "&Equals&" + bKey);
								return true;
						}
				}
				return false;
		}

		@Override
		public int hashCode()
		{
				String key = this.toString();
				return key.hashCode();
		}

		@Override
		public String toString()
		{
				if(arguments != null)
				{
						return method.toString() + "->" + arguments + " ParamActive:(" + Arrays.toString(usedArguments) + ")";
				}
				else
				{
						return method.toString() + "->" + " ParamActive:(none)";
				}
		}
}

