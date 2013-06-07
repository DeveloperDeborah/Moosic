package no.runsafe.moosic;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.output.IOutput;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;
import no.runsafe.moosic.customjukebox.CustomRecordHandler;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		Plugin.output = this.getComponent(IOutput.class);
		this.addComponent(MusicHandler.class);
		this.addComponent(CustomRecordHandler.class);

		// Commands
		this.addComponent(PlaySong.class);
		this.addComponent(StopSong.class);
	}

	public void trackStop(int playerID)
	{
		this.getComponent(CustomRecordHandler.class).onTrackPlayerStopped(playerID);
	}

	public static IOutput output;
}
