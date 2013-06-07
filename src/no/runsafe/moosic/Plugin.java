package no.runsafe.moosic;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		this.addComponent(MusicHandler.class);

		// Commands
		this.addComponent(PlaySong.class);
		this.addComponent(StopSong.class);
	}
}
