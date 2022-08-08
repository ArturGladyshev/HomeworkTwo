package SalaryHtmlReportNotifier;

import javax.mail.internet.MimeMessage;

public interface Sender
{
		void setHost(String host);

		MimeMessage createMimeMessage();

		void send(MimeMessage mimeMessage);
}
