package no.runsafe.moosic;

import no.runsafe.framework.api.ILocation;

public class NoteBlockSound
{
	public NoteBlockSound(NoteBlockInstrument instrument, byte key, byte volume, float pitch)
	{
		this.instrument = instrument;
		this.key = key;
		this.volume = volume;
		this.pitch = pitch;
	}

	public void playAtLocation(ILocation location, float volume)
	{
		float pitch = (float) Math.pow(2.0, ((double) (key - 33) - 12.0) / 12.0);

		//apply pitch shift
		pitch *= this.pitch;

		// check if pitch is out of range, bring it in range
		// 1 = one octave of separation
		if (pitch < 0)
			pitch = pitch + 1F;
		else if (pitch > 2)
			pitch = pitch - 1F;

		location.getWorld().playSound(location, this.instrument.getSound(), (volume * this.volume) / 100, pitch);
	}

	private final NoteBlockInstrument instrument;
	private final byte key;
	private final byte volume;
	private final float pitch;
}
