package no.runsafe.moosic;

import no.runsafe.framework.api.ILocation;

public class NoteBlockSound
{
	public NoteBlockSound(NoteBlockInstrument instrument, byte key, byte volume, short pitch)
	{
		this.instrument = instrument;
		this.key = key;
		this.volume = volume;
		this.pitch = pitch;
	}

	public void playAtLocation(ILocation location, float volume)
	{
		float pitch = (float) Math.pow(2.0, ((double) (key - 33) - 12.0) / 12.0);
		if (this.pitch != 0)
			pitch *= this.pitch;
		location.getWorld().playSound(location, this.instrument.getSound(), (volume * this.volume) / 100, pitch);
	}

	private final NoteBlockInstrument instrument;
	private final byte key;
	private final byte volume;
	private final short pitch;
}
