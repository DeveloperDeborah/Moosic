package no.runsafe.moosic;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.output.IOutput;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;
import no.runsafe.moosic.customjukebox.CustomJukeboxRepository;
import no.runsafe.moosic.customjukebox.CustomRecordHandler;
import no.runsafe.moosic.commands.MakeRecord;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		Plugin.output = this.getComponent(IOutput.class);

		// Repositories
		this.addComponent(CustomJukeboxRepository.class);

		this.addComponent(MusicHandler.class);
		this.addComponent(CustomRecordHandler.class);

		// Commands
		this.addComponent(PlaySong.class);
		this.addComponent(StopSong.class);
		this.addComponent(MakeRecord.class);
	}

	public void trackStop(int playerID)
	{
		this.getComponent(CustomRecordHandler.class).onTrackPlayerStopped(playerID);
	}

	public static IOutput output;
}
