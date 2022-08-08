package SalaryHtmlReportNotifier;

import java.sql.SQLException;


//Генерирует результат запроса из БД и передает результат следующему Хэндлеру

public class GenerateHtmlHandler implements Handler
{
    SendHandler sendHandler;

    public GenerateHtmlHandler(SendHandler sendListeners)
    {
        this.sendHandler = sendListeners;
    }

    @Override
    public void handle(SalaryHtmlReportNotifier salaryHtmlReportNotifier)
    {
        StringBuilder resultingHtml = new StringBuilder();
        resultingHtml.append("<html><body><table><tr><td>Employee</td><td>Salary</td></tr>");
        double totals = 0;
        try
        {
            while(salaryHtmlReportNotifier.getResults().next())
            {
                resultingHtml.append("<tr>");
                resultingHtml.append("<td>").append(salaryHtmlReportNotifier.getResults().getString("emp_name")).append("</td>");
                resultingHtml.append("<td>").append(salaryHtmlReportNotifier.getResults().getDouble("salary")).append("</td>");
                resultingHtml.append("</tr>");
                totals += salaryHtmlReportNotifier.getResults().getDouble("salary");
            }
            resultingHtml.append("<tr><td>Total</td><td>").append(totals).append("</td></tr>");
            resultingHtml.append("</table></body></html>");
        }
        catch(SQLException throwable)
        {
            throwable.printStackTrace();
        }
        System.out.println("GenerateHandler Create");
        sendHandler.sendTo(resultingHtml.toString(), salaryHtmlReportNotifier.getReport().getRecipients());
        System.out.println("Message sent");
    }
}
