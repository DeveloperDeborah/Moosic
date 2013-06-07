package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.server.RunsafeLocation;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.item.meta.RunsafeItemMeta;

import java.util.List;

public class CustomJukebox
{
	public CustomJukebox(RunsafeLocation location, RunsafeItemStack item)
	{
		this.location = location;
		this.item = item;
	}

	public RunsafeLocation getLocation()
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
		RunsafeItemMeta meta = this.item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore.size() > 0)
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

	private RunsafeLocation location;
	private RunsafeItemStack item;
	private int playerID;
}
