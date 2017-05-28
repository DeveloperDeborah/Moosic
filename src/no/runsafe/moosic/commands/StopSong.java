package no.runsafe.moosic.commands;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.WholeNumber;
import no.runsafe.moosic.MusicHandler;

public class StopSong extends ExecutableCommand
{
	public StopSong(MusicHandler musicHandler)
	{
		super(
			"stopsong", "Stops a song using its player ID", "runsafe.moosic.stop",
			new WholeNumber("playerID").require()
		);
		this.musicHandler = musicHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		Integer playerID = parameters.getValue("playerID");
		if (playerID == null)
			return "&cInvalid playerID.";
		if (this.musicHandler.forceStop(playerID))
			return "&2Stopping song with player ID " + playerID;

		return "&cCannot stop song. No player found with ID " + playerID;
	}

	private final MusicHandler musicHandler;
}
