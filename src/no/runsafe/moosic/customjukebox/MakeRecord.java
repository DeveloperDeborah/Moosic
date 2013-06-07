package no.runsafe.moosic.customjukebox;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.item.meta.RunsafeItemMeta;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;
import java.util.List;

public class MakeRecord extends PlayerCommand implements IConfigurationChanged
{
	public MakeRecord()
	{
		super("makerecord", "Forges a record with the item currently being held.", "runsafe.moosic.record.make", "song");
	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters)
	{
		RunsafeItemStack item = executor.getItemInHand();
		RunsafeItemMeta meta = item.getItemMeta();

		List<String> lore = meta.getLore();
		lore.set(0, parameters.get("song"));
		meta.setLore(lore);
		meta.setDisplayName(this.customRecordName);
		item.setItemMeta(meta);
		return "&2Success!";
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.customRecordName = configuration.getConfigValueAsString("customRecordName");
	}

	private String customRecordName;
}
