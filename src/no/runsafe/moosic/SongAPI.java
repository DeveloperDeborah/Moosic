package no.runsafe.moosic;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.lua.FunctionParameters;
import no.runsafe.framework.api.lua.Library;
import no.runsafe.framework.api.lua.RunsafeLuaFunction;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongAPI extends Library
{
	public SongAPI(RunsafePlugin plugin)
	{
		super(plugin, "song");
	}

	@Override
	protected LuaTable getAPI()
	{
		LuaTable lib = new LuaTable();
		lib.set("playSong", new PlaySong());
		lib.set("stopSong", new StopSong());
		return lib;
	}

	private static class PlaySong extends RunsafeLuaFunction
	{
		// location, songFile, volume
		// Returns int
		@Override
		public List<Object> run(FunctionParameters parameters)
		{
			List<Object> returnValues = new ArrayList<>();
			ILocation location = parameters.getLocation(0);

			File songFile = musicHandler.loadSongFile(parameters.getString(4));
			if (!songFile.exists())
				throw new LuaError("Music file not found.");

			try
			{
				returnValues.add(musicHandler.startSong(new MusicTrack(songFile), location, parameters.getFloat(5)));
			}
			catch (Exception e)
			{
				throw new LuaError(e.getMessage());
			}

			return returnValues;
		}
	}

	private static class StopSong extends RunsafeLuaFunction
	{
		@Override
		public List<Object> run(FunctionParameters parameters)
		{
			int playerID = parameters.getInt(0);
			if (musicHandler.playerExists(playerID))
				musicHandler.forceStop(playerID);

			return null;
		}
	}

	public static MusicHandler musicHandler;
}
