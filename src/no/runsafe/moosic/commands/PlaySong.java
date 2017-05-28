package no.runsafe.moosic.commands;

import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.moosic.MusicHandler;
import no.runsafe.moosic.MusicTrack;

import java.io.File;

public class PlaySong extends PlayerCommand
{
	public PlaySong(MusicHandler musicHandler)
	{
		super(
			"playsong", "Plays a song at your location.", "runsafe.moosic.play",
				new RequiredArgument("volume"),
				new TrailingArgument("song").require()
		);
		this.musicHandler = musicHandler;

	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		File song = this.musicHandler.loadSongFile(parameters.get("song"));

		if (!song.exists())
			return "&cThat song does not exist.";

		try
		{
			int id = this.musicHandler.startSong(
				new MusicTrack(song),
				executor.getLocation(),
				Float.valueOf(parameters.get("volume"))
			);
			return "&2Playing song with player ID: " + id;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "&cThere was an error! Seek admin assistance.";
		}
	}

	private final MusicHandler musicHandler;
}
