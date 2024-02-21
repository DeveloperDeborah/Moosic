package no.runsafe.moosic;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.log.IConsole;

import java.io.File;
import java.util.HashMap;

public class MusicHandler
{
	public MusicHandler(IScheduler scheduler, Moosic moosic, IConsole output)
	{
		this.scheduler = scheduler;
		this.path = String.format("plugins/%s/songs/", moosic.getName());
		this.moosic = moosic;

		File pathDir = new File(path);
		if (!pathDir.exists())
			if (!pathDir.mkdirs())
				output.logWarning("&cUnable to create directories at %s", path);
	}

	public File loadSongFile(String fileName)
	{
		return new File(path + fileName);
	}

	public int startSong(MusicTrack musicTrack, ILocation location, float volume)
	{
		TrackPlayer trackPlayer = new TrackPlayer(location, musicTrack, volume);
		final int newID = currentTrackPlayerID + 1;

		double tickDelay = 1.0 / (double) musicTrack.getTempo();
		tickDelay = tickDelay * 20D;
		long delay = (long) tickDelay;
		int timer = scheduler.startSyncRepeatingTask(() -> progressPlayer(newID), delay, delay);

		trackPlayer.setTimerID(timer);
		trackPlayers.put(newID, trackPlayer);

		currentTrackPlayerID = newID;
		return currentTrackPlayerID;
	}

	public boolean forceStop(int playerID)
	{
		if (trackPlayers.containsKey(playerID))
		{
			stopPlayer(playerID);
			return true;
		}
		return false;
	}

	public boolean playerExists(int playerID)
	{
		return trackPlayers.containsKey(playerID);
	}

	public void progressPlayer(int playerID)
	{
		TrackPlayer trackPlayer = trackPlayers.get(playerID);
		if (!trackPlayer.playNextTick())
			stopPlayer(playerID);
	}

	private void stopPlayer(int playerID)
	{
		TrackPlayer trackPlayer = trackPlayers.get(playerID);
		scheduler.cancelTask(trackPlayer.getTimerID());
		trackPlayers.remove(playerID);
		moosic.trackStop(playerID);
	}

	private final IScheduler scheduler;
	private final HashMap<Integer, TrackPlayer> trackPlayers = new HashMap<>();
	private int currentTrackPlayerID = 0;
	private final String path;
	private final Moosic moosic;
}
