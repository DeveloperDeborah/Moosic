package no.runsafe.moosic;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Events;
import no.runsafe.moosic.commands.MakeRecord;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;
import no.runsafe.moosic.customjukebox.CustomJukeboxRepository;
import no.runsafe.moosic.customjukebox.CustomRecordHandler;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		addComponent(Events.class);
		addComponent(Commands.class);

		// Repositories
		this.addComponent(CustomJukeboxRepository.class);

		this.addComponent(MusicHandler.class);
		this.addComponent(CustomRecordHandler.class);
		this.addComponent(SongAPI.class);

		// Commands
		this.addComponent(PlaySong.class);
		this.addComponent(StopSong.class);
		this.addComponent(MakeRecord.class);
	}

	public void trackStop(int playerID)
	{
		this.getComponent(CustomRecordHandler.class).onTrackPlayerStopped(playerID);
	}
}
