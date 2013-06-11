package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.internal.database.Repository;
import no.runsafe.framework.internal.database.Row;
import no.runsafe.framework.internal.database.Set;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.RunsafeWorld;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.inventory.RunsafeInventoryType;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomJukeboxRepository extends Repository
{
	public CustomJukeboxRepository(IDatabase database)
	{
		this.database = database;
	}

	@Override
	public String getTableName()
	{
		return "moosic_jukeboxes";
	}

	public void storeJukebox(RunsafeLocation location, RunsafeMeta item)
	{
		RunsafeInventory holder = RunsafeServer.Instance.createInventory(null, RunsafeInventoryType.CHEST);
		holder.addItems(item);

		this.database.Execute(
			"INSERT INTO `moosic_jukeboxes` (world, x, y, z, item) VALUES(?, ?, ?, ?, ?)",
			location.getWorld().getName(),
			location.getBlockX(),
			location.getBlockY(),
			location.getBlockZ(),
			holder.serialize()
		);
	}

	public void deleteJukeboxes(RunsafeLocation location)
	{
		this.database.Execute(
			"DELETE FROM `moosic_jukeboxes` WHERE world = ? AND x = ? AND y = ? AND z = ?",
			location.getWorld().getName(),
			location.getBlockX(),
			location.getBlockY(),
			location.getBlockZ()
		);
	}

	public List<CustomJukebox> getJukeboxes()
	{
		List<CustomJukebox> jukeboxes = new ArrayList<CustomJukebox>();
		Set data = this.database.Query("SELECT world, x, y, z, item FROM moosic_jukeboxes");

		if (data != null)
		{
			for (Row node : data)
			{
				RunsafeWorld world = RunsafeServer.Instance.getWorld(node.String("world"));
				if (world != null)
				{
					RunsafeLocation location = new RunsafeLocation(world, node.Double("x"), node.Double("y"), node.Double("z"));
					RunsafeInventory holder = RunsafeServer.Instance.createInventory(null, RunsafeInventoryType.CHEST);
					holder.unserialize(node.String("item"));

					jukeboxes.add(new CustomJukebox(location, holder.getContents().get(0)));
				}
			}
		}
		return jukeboxes;
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> versions = new HashMap<Integer, List<String>>();
		ArrayList<String> sql = new ArrayList<String>();
		sql.add(
			"CREATE TABLE `moosic_jukeboxes` (" +
				"`world` VARCHAR(50) NOT NULL," +
				"`x` DOUBLE NOT NULL," +
				"`y` DOUBLE NOT NULL," +
				"`z` DOUBLE NOT NULL," +
				"`item` longtext" +
				")"
		);
		versions.put(1, sql);
		return versions;
	}

	private final IDatabase database;
}
