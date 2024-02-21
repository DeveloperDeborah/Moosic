package no.runsafe.moosic.commands;

import no.runsafe.framework.api.command.argument.DecimalNumber;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.log.Console;
import no.runsafe.moosic.MusicHandler;
import no.runsafe.moosic.MusicTrack;

import java.io.File;

public class PlaySong extends PlayerCommand
{
	public PlaySong(MusicHandler musicHandler)
	{
		super(
			"playsong", "Plays a song at your location.", "runsafe.moosic.play",
			new DecimalNumber("volume").withDefault(1.0F).require(),
			new TrailingArgument("song").require()
		);
		this.musicHandler = musicHandler;

	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		String file = parameters.getRequired("song");
		float volume = parameters.getRequired("volume");

		File song = this.musicHandler.loadSongFile(file);
		if (!song.exists())
			return "&cThat song does not exist.";

		try
		{
			int id = this.musicHandler.startSong(
				new MusicTrack(song),
				executor.getLocation(),
				volume
			);
			return "&2Playing song with player ID: " + id;
		}
		catch (Exception e)
		{
			Console.Global().logException(e);
			return "&cThere was an error! Seek admin assistance.";
		}
	}

	private final MusicHandler musicHandler;
}
