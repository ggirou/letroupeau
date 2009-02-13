package Sound;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

import config.Constants;

/**
 * 
 * @author GGirou
 * 
 */
public class MainSound {

	public void initialize() {
		// Initialize
		try {
			AL.create();

			System.out.println("Default device: "
					+ ALC10.alcGetString(null,
							ALC10.ALC_DEFAULT_DEVICE_SPECIFIER));

			if (ALC10.alcIsExtensionPresent(null, "ALC_ENUMERATION_EXT")) {
				String[] devices = ALC10.alcGetString(null,
						ALC10.ALC_DEVICE_SPECIFIER).split("\0");
				System.out.println("Available devices: ");
				for (int i = 0; i < devices.length; i++) {
					System.out.println(i + ": " + devices[i]);
				}
			}
		} catch (Exception e) {
			System.out
					.println("Unable to create OpenAL.\nPlease make sure that OpenAL is available on this system. Exception: "
							+ e);
			return;
		}

		// Play sound
		int lastError;

		Vector3f mePosition = new Vector3f();
		Vector3f sheepPosition = new Vector3f();

		IntBuffer buffers = BufferUtils.createIntBuffer(1);
		IntBuffer sources = BufferUtils.createIntBuffer(1);

		// al generate buffers and sources
		buffers.position(0).limit(1);
		AL10.alGenBuffers(buffers);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

		sources.position(0).limit(1);
		AL10.alGenSources(sources);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

		WaveData wavefile = WaveData.create(Constants.SOUND_PATH
				+ "lamb_little.wav");

		// copy to buffers
		AL10.alBufferData(buffers.get(0), wavefile.format, wavefile.data,
				wavefile.samplerate);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

		// unload file again
		wavefile.dispose();

		// set up source input
		AL10.alSourcei(sources.get(0), AL10.AL_BUFFER, buffers.get(0));
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

		AL10.alSourcef(sources.get(0), AL10.AL_REFERENCE_DISTANCE, 1024.0f);
		AL10.alSourcef(sources.get(0), AL10.AL_ROLLOFF_FACTOR, 0.5f);

		// lets loop the sound
		AL10.alSourcei(sources.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

		// play source 0
		AL10.alSourcePlay(sources.get(0));
		if ((lastError = AL10.alGetError()) != AL10.AL_NO_ERROR) {
			System.exit(lastError);
		}

	}
}
