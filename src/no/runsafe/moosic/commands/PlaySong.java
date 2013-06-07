package no.runsafe.moosic.commands;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.moosic.MusicHandler;
import no.runsafe.moosic.MusicTrack;

import java.io.File;
import java.util.HashMap;

public class PlaySong extends PlayerCommand
{
	public PlaySong(MusicHandler musicHandler)
	{
		super("playsong", "Plays a song at your location.", "runsafe.moosic.play", "song", "volume", "speed");
		this.musicHandler = musicHandler;

	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters)
	{
		File song = this.musicHandler.loadSongFile(parameters.get("song"));

		if (!song.exists())
			return "&cThat song does not exist.";

		try
		{
			int id = this.musicHandler.startSong(
					new MusicTrack(song),
					executor.getLocation(),
					Float.valueOf(parameters.get("volume")),
					Integer.valueOf(parameters.get("speed"))
			);
			return "&2Playing song with player ID: " + id;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "&cThere was an error! Seek admin assistance.";
		}
	}

	private MusicHandler musicHandler;
}
