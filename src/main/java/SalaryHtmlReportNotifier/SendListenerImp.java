package SalaryHtmlReportNotifier;

import javax.mail.internet.MimeMessage;

//Слушатель, использующий MessageHelper и SenderImp для создания и отправки сообщения

public class SendListenerImp implements SendListener
{

    String password;

    String login;

    public SendListenerImp(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

    @Override
    public void sendResultingHtmlTo(String resultHtml, String recipients)
    {
        SenderImp senderImp = new SenderImp(login, password);
        MimeMessage mimeMessage = senderImp.createMimeMessage();
        MessageHelper messageHelper = new MessageHelper(mimeMessage);
        messageHelper.setText(resultHtml);
        messageHelper.setTo(recipients);
        messageHelper.setSubject("Monthly department salary report");
        senderImp.send(mimeMessage);
    }
}
