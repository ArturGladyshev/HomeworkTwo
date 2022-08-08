package SalaryHtmlReportNotifier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

//TODO Классы, описанные в папке DaoCache связанны с одной задачей и тестируются в test.java

public class SalaryHtmlReportNotifier
{
		private Connection connection;

		private Report report;

		private ResultSet results = null;

		public SalaryHtmlReportNotifier(Connection databaseConnection, Report report) throws SQLException
		{
				this.connection = databaseConnection;
				this.report = report;
		}

		public void setResults(ResultSet results)
		{
				this.results = results;
		}

		public ResultSet getResults()
		{
				return results;
		}

		public Connection getConnection()
		{
				return connection;
		}

		public Report getReport()
		{
				return report;
		}
}

