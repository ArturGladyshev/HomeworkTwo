package DaoCache;

public class NumberAccumulatorImp implements NumberAccumulator
{
		//Тестируемый класс
		@Override
		public int sumOfNumbers(int one, int two)
		{
				System.out.println("Метод sumOfNumber(Class.Integer , Class.Integer)");
				try
				{
						Thread.sleep(2000);
				}
				catch(
					InterruptedException e)
				{
						e.printStackTrace();
				}
				return one + two;
		}

		@Override
		public int sumOfNumbers(int one, double two)
		{
				System.out.println("Метод sumOfNumber(Class.Integer , Class.Double)");
				try
				{
						Thread.sleep(2000);
				}
				catch(InterruptedException e)
				{
						e.printStackTrace();
				}
				return one + (int)two;
		}
}
