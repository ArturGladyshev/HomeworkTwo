package SalaryHtmlReportNotifier;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


//Создает запрос в БД и передает результат следующему Хэндлеру

public class SelectSQLHandler implements Handler
{
    private final Handler next;

    public SelectSQLHandler(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(SalaryHtmlReportNotifier salaryHtmlReportNotifier) {
        PreparedStatement ps;
        try {
            ps = salaryHtmlReportNotifier.getConnection().prepareStatement("select emp.id as emp_id, emp.name as amp_name, sum(salary) as salary from employee emp left join" +
                    "salary_payments sp on emp.id = sp.employee_id where emp.department_id = ? and" +
                    " sp.date >= ? and sp.date <= ? group by emp.id, emp.name");

            ps.setString(0, salaryHtmlReportNotifier.getReport().getDepartmentId());
            ps.setDate(1, new Date(salaryHtmlReportNotifier.getReport().getDateFrom().toEpochDay()));
            ps.setDate(2, new Date(salaryHtmlReportNotifier.getReport().getDateTo().toEpochDay()));
            salaryHtmlReportNotifier.setResults(ps.executeQuery());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        System.out.println("SQLHandler Create");
        next.handle(salaryHtmlReportNotifier);
    }
}