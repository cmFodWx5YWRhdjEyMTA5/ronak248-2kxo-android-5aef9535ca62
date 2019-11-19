package com.screamxo.AudioRecorder;

/**
 * Created by Shubham.Agarwal on 20/10/16.
 */

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;


public class AudioResetRecorder {
    public static MediaRecorder mediaRecorder;
    public static File audioFile;

    public static MediaRecorder resetRecorderforMcq(String name) {

        audioFile = new File(Environment.getExternalStorageDirectory(),
                "ata_audio_mcq" + name + ".m4a");
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();

        }
        return mediaRecorder;
    }

    public static File getAudioPathforPostMedia() {
        if (audioFile != null) {
            return audioFile.getAbsoluteFile();
        } else {
            return null;
        }
    }
}
