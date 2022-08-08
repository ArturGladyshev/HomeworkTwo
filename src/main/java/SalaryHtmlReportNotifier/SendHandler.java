package SalaryHtmlReportNotifier;

import java.util.List;
//Передает результат работы предыдущего Хэндлера Слушателям

public class SendHandler
{
    private List<SendListener> sendListeners;

    public void addSendListener(SendListener sendListener)
    {
        this.sendListeners.add(sendListener);
    }

    public void removeSendListener(SendListener sendListener)
    {
        this.sendListeners.remove(sendListener);
    }

    public void sendTo(String resultHtml, String recipients)
    {
        for(SendListener sendListener : this.sendListeners)
        {
            sendListener.sendResultingHtmlTo(resultHtml, recipients);
        }
    }
}
