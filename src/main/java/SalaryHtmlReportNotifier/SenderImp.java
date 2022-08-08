package SalaryHtmlReportNotifier;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//Создает и возвращает MineMessage

public class SenderImp implements Sender
{

		private Properties props;

		private String username;

		private String password;

		private Session session;

		public SenderImp(String username, String password)
		{
				this.username = username;
				this.password = password;
				props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				session = Session.getDefaultInstance(props, new Authenticator()
				{
						protected PasswordAuthentication getPasswordAuthentication()
						{
								return new PasswordAuthentication(username, password);
						}
				});
		}

		@Override
		public void setHost(String host)
		{
				props.setProperty("mail.smtp.host", host);
				session = Session.getDefaultInstance(props);
		}

		@Override
		public MimeMessage createMimeMessage()
		{
				return new MimeMessage(session);
		}

		@Override
		public void send(MimeMessage mimeMessage)
		{
				try
				{
						Transport.send(mimeMessage);
				}
				catch(MessagingException e)
				{
						e.printStackTrace();
				}
		}
}

