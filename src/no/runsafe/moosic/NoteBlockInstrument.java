package no.runsafe.moosic;

import no.runsafe.framework.minecraft.Sound;

import java.util.ArrayList;
import java.util.List;

public class NoteBlockInstrument
{
	public NoteBlockInstrument(short soundID)
	{
		this.soundID = soundID;
	}

	public Sound getSound()
	{
		return NoteBlockInstrument.sounds.get(this.soundID);
	}

	private final int soundID;
	private static final List<Sound> sounds = new ArrayList<Sound>();
	static
	{
		sounds.add(Sound.Note.Piano);
		sounds.add(Sound.Note.BassGuitar);
		sounds.add(Sound.Note.BassDrum);
		sounds.add(Sound.Note.Snare);
		sounds.add(Sound.Note.Sticks);
		sounds.add(Sound.Note.Chime);
		sounds.add(Sound.Note.Flute);
		sounds.add(Sound.Note.Xylophone);
		sounds.add(Sound.Note.Bass);
		sounds.add(Sound.Note.Bell);
		sounds.add(Sound.Note.Guitar);
		sounds.add(Sound.Note.Pling);
	}
}
