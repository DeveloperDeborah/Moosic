package no.runsafe.moosic;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;
import no.runsafe.moosic.commands.MakeRecord;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;
import no.runsafe.moosic.customjukebox.CustomJukeboxRepository;
import no.runsafe.moosic.customjukebox.CustomRecordHandler;

public class Moosic extends RunsafeConfigurablePlugin
{
	public static IDebug Debugger = null;

	@Override
	protected void pluginSetup()
	{
		Debugger = getComponent(IDebug.class);

		addComponent(Events.class);
		addComponent(Commands.class);
		addComponent(Database.class);
		addComponent(Config.class);

		// Repositories
		addComponent(CustomJukeboxRepository.class);

		addComponent(MusicHandler.class);
		addComponent(CustomRecordHandler.class);
		addComponent(SongAPI.class);

		// Commands
		addComponent(PlaySong.class);
		addComponent(StopSong.class);
		addComponent(MakeRecord.class);
	}

	public void trackStop(int playerID)
	{
		getComponent(CustomRecordHandler.class).onTrackPlayerStopped(playerID);
	}
}
