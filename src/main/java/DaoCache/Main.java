package DaoCache;

import org.junit.Test;

/* Задача: реализовать аннотацию Caching, содержащую значение Class<? extends Source> source(),
 Интерфейс Source должен содержать методы для работы с БД: добавление\выборка значений и создание БД.
 Также нужно создать класс для создания прокси объектов: результаты работы методов объекта должны
 добавляться в кэш при наличии аннотации над методом. Для методов, находящих последовательность
 Фиббоначи должно использоваться значение searchFibonacciSequence(). Если оно = true, то вместо стандартного
 кэширования должна вычисляться последовательность Фиббоначи и добавляться в таблицу чисел БД.
 В рамках задачи нужно реализовать класс, имплементирующий Source и реализовывающий работу с БД
 на основе JDBC.
 */

public class Main
{
    @Test
    public void testing()
    {
        NumberAccumulatorImp t = new NumberAccumulatorImp();
        NumberAccumulator accumulator = ProxyCache.getProxyInstance(t);
        System.out.println(accumulator.sumOfNumbers(10, 10));
        System.out.println(accumulator.sumOfNumbers(200, 10));
        System.out.println(accumulator.sumOfNumbers(10, 200));
        System.out.println(accumulator.sumOfNumbers(10, 10));
        System.out.println(accumulator.sumOfNumbers(10, 10));
        System.out.println(accumulator.sumOfNumbers(10, 10));
        System.out.println(accumulator.sumOfNumbers(10, 10));
        System.out.println(accumulator.sumOfNumbers(40, 40));
        System.out.println(accumulator.printNull());
        System.out.println(accumulator.sumOfNumbers(10, 10f));
        System.out.println(accumulator.printNull());
    }

    @Test
    public void testing2()
    {
        ListFibonacciImp l = new ListFibonacciImp();
        FibonacciSequenceFinder sequenceFinder = ProxyCache.getProxyInstance(l);
        sequenceFinder.findFibonacciSequence(8l);
        sequenceFinder.findFibonacciSequence(15l);
        sequenceFinder.findFibonacciSequence(1l);
        sequenceFinder.findFibonacciSequence(7l);
        sequenceFinder.findFibonacciSequence(20l);
        sequenceFinder.findFibonacciSequence(15l);
        sequenceFinder.findFibonacciSequence(10l);
        sequenceFinder.findFibonacciSequence(21l);
    }
}
