package no.runsafe.moosic;

import no.runsafe.framework.server.RunsafeLocation;

public class NoteBlockSound
{
	public NoteBlockSound(NoteBlockInstrument instrument, byte key)
	{
		this.instrument = instrument;
		this.key = key;
	}

	public void playAtLocation(RunsafeLocation location, float volume)
	{
		float pitch = (float) Math.pow(2.0, ((double) (key - 33) - 12.0) / 12.0);
		location.getWorld().playSound(location, this.instrument.getSound(), volume, pitch);
	}

	private NoteBlockInstrument instrument;
	private byte key;
}