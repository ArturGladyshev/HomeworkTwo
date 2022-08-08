import SalaryHtmlReportNotifier.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/* Задача: реализовать паттерн "цепочка обязанностей": найти в базе данных работников и их зарплаты,
на основе полученных данных сформировать таблицу html, отправить всем работникам на почту smtp.gmail.com
таблицу в виде MimeMessage. Реализованный функционал должен тестироваться средствами
библиотеки Mockito и junit.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(SalaryHtmlReportNotifier.class)
public class TestSalaryHtmlReportNotifier
{
    @Test
    public void test() throws Exception
    {
        //На основе фиктивной базы данный создается SalaryHtmlReportNotifier
        Connection someFakeConnection = mock(Connection.class);
        ResultSet mockResultSet = getMockedResultSet(someFakeConnection);
        when(mockResultSet.getString("emp_name")).thenReturn("John Doe", "Jane Dow");
        when(mockResultSet.getDouble("salary")).thenReturn(100.0, 100.0, 50.0, 50.0);
        LocalDate dateFrom = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate dateTo = LocalDate.of(2014, Month.DECEMBER, 31);
        Report report = new Report("10", dateFrom, dateTo, "somebody@gmail.com");
        //Запуск цепочки Хэндлеров и проверка отработки цепочки методов
        SendListenerImp fakeSendListenerImp = mock(SendListenerImp.class);
        SalaryHtmlReportNotifier salaryHtmlReportNotifier = new SalaryHtmlReportNotifier(someFakeConnection, report);
        SendHandler fakeSendHandler = mock(SendHandler.class);
        fakeSendHandler.addSendListener(fakeSendListenerImp);
        SelectSQLHandler selectSQLHandler = new SelectSQLHandler(new GenerateHtmlHandler(fakeSendHandler));
        selectSQLHandler.handle(salaryHtmlReportNotifier);
        ArgumentCaptor<String> messageTextArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(fakeSendHandler).sendTo(messageTextArgumentCaptor.capture(), any());
    }

    private ResultSet getMockedResultSet(Connection someFakeConnection) throws SQLException
    {
        //Тестирование фиктивной базы данных
        PreparedStatement someFakePreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(someFakePreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(someFakeConnection.prepareStatement(anyString())).thenReturn(someFakePreparedStatement);
        when(mockResultSet.next()).thenReturn(true, true, false);
        return mockResultSet;
    }

    private MessageHelper getMockedMimeMessageHelper() throws Exception
    {
        /*Тестирование внутренней логики слушателя: метод не пригодился, так как
        внутренние mock-объекты MessageHelper создаются автоматически.
         */
        SenderImp mockMailSender = mock(SenderImp.class);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        MessageHelper mockMimeMessageHelper = mock(MessageHelper.class);
        whenNew(MessageHelper.class).withArguments(mockMimeMessage).thenReturn(mockMimeMessageHelper);
        SendListenerImp mockListener = mock(SendListenerImp.class);
        whenNew(SendListenerImp.class).withArguments("sdfdsf", "sdfsdf").thenReturn(mockListener);
        return mockMimeMessageHelper;
    }
}


