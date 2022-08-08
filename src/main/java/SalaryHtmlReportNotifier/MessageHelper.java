package SalaryHtmlReportNotifier;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//Помогает настроить MineMessage

public class MessageHelper
{
		private MimeMessage mimeMessage;

		public MessageHelper(MimeMessage mimeMessage)
		{
				this.mimeMessage = mimeMessage;
		}

		public void setTo(String recipients)
		{
				try
				{
						mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
				}
				catch(MessagingException e)
				{
						System.out.println("Ошибка в списке отправителей");
						e.printStackTrace();
				}
		}

		public void setText(String result)
		{
				try
				{
						mimeMessage.setText(result);
				}
				catch(MessagingException e)
				{
						System.out.println("Ошибка в тексте сообщения");
						e.printStackTrace();
				}
		}

		public void setSubject(String subject)
		{
				try
				{
						mimeMessage.setSubject(subject);
				}
				catch(MessagingException e)
				{
						e.printStackTrace();
				}
		}
}
