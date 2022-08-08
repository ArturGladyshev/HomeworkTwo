package SalaryHtmlReportNotifier;

//Интерфейс слушателя

public interface SendListener
{

    void sendResultingHtmlTo(String resultHtml, String recipients);
}
