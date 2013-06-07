package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerRightClickBlock;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.server.RunsafeLocation;
import no.runsafe.framework.server.block.RunsafeBlock;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.item.meta.RunsafeItemMeta;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.moosic.ITrackPlayerStopped;
import no.runsafe.moosic.MusicHandler;
import no.runsafe.moosic.MusicTrack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomRecordHandler implements IConfigurationChanged, IPlayerRightClickBlock, ITrackPlayerStopped
{
	public CustomRecordHandler(MusicHandler musicHandler)
	{
		this.musicHandler = musicHandler;
	}

	@Override
	public boolean OnPlayerRightClick(RunsafePlayer player, RunsafeItemStack usingItem, RunsafeBlock targetBlock)
	{
		if (targetBlock.is(Item.Decoration.Jukebox))
		{
			CustomJukebox jukebox = this.getJukeboxAtLocation(targetBlock.getLocation());
			if (jukebox != null)
			{
				int playerID = jukebox.getPlayerID();
				if (this.musicHandler.playerExists(playerID))
					this.musicHandler.forceStop(playerID);

				jukebox.ejectRecord();
				this.jukeboxes.remove(jukebox);
				return false;
			}
			else
			{
				if (CustomRecordHandler.recordItems.contains(usingItem.getItemType()))
				{
					if (this.isCustomRecord(usingItem))
					{
						jukebox = this.playJukebox(player, new CustomJukebox(targetBlock.getLocation(), usingItem));
						this.jukeboxes.add(jukebox);
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isCustomRecord(RunsafeItemStack item)
	{
		RunsafeItemMeta itemMeta = item.getItemMeta();
		return itemMeta.getDisplayName().equalsIgnoreCase(this.customRecordName);
	}

	public CustomJukebox getJukeboxAtLocation(RunsafeLocation location)
	{
		for (CustomJukebox jukebox : this.jukeboxes)
			if (jukebox.getLocation().distance(location) < 1)
				return jukebox;

		return null;
	}

	private CustomJukebox playJukebox(RunsafePlayer player, CustomJukebox jukebox)
	{
		File musicFile = this.musicHandler.loadSongFile(jukebox.getSongName() + ".nbs");
		if (musicFile.exists())
		{
			MusicTrack musicTrack = null;
			try
			{
				musicTrack = new MusicTrack(musicFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (musicTrack != null)
				jukebox.setPlayerID(this.musicHandler.startSong(musicTrack, jukebox.getLocation(), 65));
		}
		else
		{
			// Corrupt record, presumably.
			player.sendColouredMessage("&cThe record cracks and scratches as you put it in the jukebox.");
		}
		return jukebox;
	}

	@Override
	public void onTrackPlayerStopped(int trackPlayerID)
	{
		for (CustomJukebox jukebox : this.jukeboxes)
		{
			if (jukebox.getPlayerID() == trackPlayerID)
			{
				this.jukeboxes.remove(jukebox);
				jukebox.setPlayerID(-1);
				this.jukeboxes.add(jukebox);
			}
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.customRecordName = configuration.getConfigValueAsString("customRecordName");
	}

	private List<CustomJukebox> jukeboxes = new ArrayList<CustomJukebox>();
	private String customRecordName;
	private MusicHandler musicHandler;

	private static List<Item> recordItems = new ArrayList<Item>();
	static
	{
		recordItems.add(Item.Miscellaneous.Record.C418_11);
		recordItems.add(Item.Miscellaneous.Record.C418_13);
		recordItems.add(Item.Miscellaneous.Record.C418_Blocks);
		recordItems.add(Item.Miscellaneous.Record.C418_Chirp);
		recordItems.add(Item.Miscellaneous.Record.C418_Far);
		recordItems.add(Item.Miscellaneous.Record.C418_Mall);
		recordItems.add(Item.Miscellaneous.Record.C418_Mellohi);
		recordItems.add(Item.Miscellaneous.Record.C418_Stal);
		recordItems.add(Item.Miscellaneous.Record.C418_Strad);
		recordItems.add(Item.Miscellaneous.Record.C418_Wait);
		recordItems.add(Item.Miscellaneous.Record.C418_Ward);
	}
}
