package no.runsafe.moosic;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.output.IOutput;
import no.runsafe.moosic.commands.PlaySong;
import no.runsafe.moosic.commands.StopSong;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		Plugin.output = this.getComponent(IOutput.class);
		this.addComponent(MusicHandler.class);

		// Commands
		this.addComponent(PlaySong.class);
		this.addComponent(StopSong.class);
	}

	public static IOutput output;
}
