package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import java.util.List;

public class CustomJukebox
{
	public CustomJukebox(ILocation location, RunsafeMeta item)
	{
		this.location = location;
		this.item = item;

		if (location == null)
			throw new IllegalArgumentException("A jukebox must have a location!");
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public void ejectRecord()
	{
		location.incrementY(1);
		this.location.getWorld().dropItem(this.location, this.item);
		location.decrementY(1);
	}

	public String getSongName()
	{
		List<String> lore = item.getLore();
		if (lore != null && !lore.isEmpty())
			return lore.get(0);
		return "";
	}

	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}

	public int getPlayerID()
	{
		return this.playerID;
	}

	private final ILocation location;
	private final RunsafeMeta item;
	private int playerID;
}
