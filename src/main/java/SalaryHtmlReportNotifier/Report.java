package SalaryHtmlReportNotifier;

import java.time.LocalDate;

public class Report
{
    private final String departmentId;

    private final LocalDate dateFrom;

    private final LocalDate dateTo;

    private final String recipients;

    public Report(String departmentId, LocalDate dateFrom, LocalDate dateTo, String recipients)
    {
        this.departmentId = departmentId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.recipients = recipients;
    }

    public String getDepartmentId()
    {
        return departmentId;
    }

    public LocalDate getDateFrom()
    {
        return dateFrom;
    }

    public LocalDate getDateTo()
    {
        return dateTo;
    }

    public String getRecipients()
    {
        return recipients;
    }
}
