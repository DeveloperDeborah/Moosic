package no.runsafe.moosic;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

public class Config implements IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		customRecordName = config.getConfigValueAsString("customRecordName");

		Message.musicSubtitle = config.getConfigValueAsString("message.");
		Message.musicRejected = config.getConfigValueAsString("message.");
	}

	public static final class Message
	{
		public static String getMusicSubtitle()
		{
			return musicSubtitle;
		}

		public static String getMusicRejected()
		{
			return musicRejected;
		}

		private static String musicSubtitle;
		private static String musicRejected;
	}

	public static String getCustomRecordName()
	{
		return customRecordName;
	}

	private static String customRecordName;
}
