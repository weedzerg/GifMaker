package com.studio.makergif.utils;

import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.studio.makergif.interfaces.ResultComand;
import com.studio.makergif.interfaces.UpdateProcessing;

/**
 * Created by DaiPhongPC on 10/17/2017.
 */

public class EditVideo {
    private FFmpeg ffmpeg;
    private ResultComand resultComand;
    private UpdateProcessing updateProcessing;

    public EditVideo(FFmpeg ffmpeg, ResultComand resultComand) {
        this.ffmpeg = ffmpeg;
        this.resultComand = resultComand;
    }

    public EditVideo(FFmpeg ffmpeg, ResultComand resultComand, UpdateProcessing updateProcessing) {
        this.ffmpeg = ffmpeg;
        this.resultComand = resultComand;
        this.updateProcessing = updateProcessing;
    }

    public void executeSlowMotionVideoCommand(String pathin, String pathout) {
        String[] complexCommand = {"-y", "-i", pathin, "-filter_complex",
                "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v",
                "2097k", "-r", "60", "-vcodec", "mpeg4", pathout};
        execFFmpegBinary(complexCommand, "slowmotion");
    }

//    public void executeFastMotionVideoCommand(String inpath, String outpath) {
//        Log.d("DEBUG", "startTrim: src: " + inpath);
//        Log.d("DEBUG", "startTrim: dest: " + outpath);
//        String[] complexCommand = {"-y", "-i", inpath, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outpath};
//        execFFmpegBinary(complexCommand);
//
//    }

    private void execFFmpegBinary(final String[] command, final String type) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d("DEBUG " + type, "FAILED with output : " + s);
                    resultComand.resultComand("fail");
                }

                @Override
                public void onSuccess(String s) {
                    Log.d("DEBUG", "SUCCESS with output : " + s);
                    resultComand.resultComand("ok");
                }

                @Override
                public void onProgress(String s) {
                    Log.d("DEBUG", "Process command : ffmpeg " + s);
                    if (updateProcessing != null) {
                        String resutl = "";
                        int index = s.indexOf("time=");
                        try {
                            resutl = s.substring(index, index + 16);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateProcessing.updateUI(resutl);
                    }


                }

                @Override
                public void onStart() {
                    Log.d("DEBUG", "Started command : ffmpeg " + command);

                }

                @Override
                public void onFinish() {
                    Log.d("DEBUG", "Finished command : ffmpeg " + command);


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    public void extractImagesVideo(String inpath, String outpath) {
        String[] complexCommand = {"-i", inpath, outpath, "-hide_banner"};
  /*   Remove -r 1 if you want to extract all video frames as images from the specified time duration.*/
        execFFmpegBinary(complexCommand, "image");

    }

    //    public void extractImagesVideo(String inpath, String outpath) {
//        String[] complexCommand = {"-y", "-i", inpath, "-an", "-r", "6", "-ss", "" + 0, "-t", "" + 1, outpath};
//  /*   Remove -r 1 if you want to extract all video frames as images from the specified time duration.*/
//        execFFmpegBinary(complexCommand);
//
//    }
    public void creatvideotoImage(String inpath, String pathout) {
        String[] complexCommand = {"-y", "-f", "image2", "-i", inpath,
                "-vcodec", "mpeg4", "-b:v", "2100k", pathout};
        execFFmpegBinary(complexCommand, "video");
    }

    //
//    public void extractGifVideo(String inpath, String inpath1, String outpath) {
//        String[] complexCommand = {"-y", "-i", inpath, "-i", inpath1, "-filter_complex", "paletteuse", "-r", 10 + "", outpath};
//        execFFmpegBinary(complexCommand);
//    }
//
    public void extractPalettegen(String inpath, String outpath) {
        String[] complexCommand = {"-y", "-i", inpath, "-vf", "palettegen", outpath};
//        String[] complexCommand = {"-i", inpath, outpath, "-hide_banner"};
        execFFmpegBinary(complexCommand, "gif");
    }


    public void excuteVideotoGif(String pathin, String pathout, String size) {
        String[] complexCommand = {"-i", pathin, "-r", 10 + "", "-vf", "scale=" + size, pathout};
//        String[] complexCommand = {"-i", pathin, "-vf", "scale=" + size, pathout, "-hide_banner"};
        execFFmpegBinary(complexCommand, "gif");
    }

    public void excuteVideotoGifHeight(String pathin, String pathout, String size) {
//        String[] complexCommand = {"-i", pathin, "-b", "2048k", pathout};
        String[] complexCommand = {"-i", pathin, "-vf", "scale=" + size, pathout, "-hide_banner"};
        execFFmpegBinary(complexCommand, "gif");
    }
//
//    public void excuteVideotoGif(String pathin, String pathout, String size) {
//
//        String[] complexCommand = {"-i", pathin, "-s", "hd480", pathout};
//        execFFmpegBinary(complexCommand, "gif");
//    }

    public void excuteImagetoGif(String pathin, String pathout) {
        String[] complexCommand = {"-i", pathin, pathout};
        execFFmpegBinary(complexCommand, "gif");
    }

    public void excuteVideotoScale(String pathin, String pathout, String size) {
        String[] complexCommand = {"-i", pathin, "-vf", "scale=" + size, pathout, "-hide_banner"};
        execFFmpegBinary(complexCommand, "gif");
    }

    public void excuteVideotoGifHeight(String pathin, String pathout) {
        String[] complexCommand = {"-i", pathin, "-vf", "fps=15,scale=320:-1:flags=lanczos,palettegen", "-y", pathout};
        execFFmpegBinary(complexCommand, "palettegen");
    }

    public void excuteVideotoGifHeight1(String pathin, String path1, String pathout) {
        String[] complexCommand = {"-i", pathin, "-i", path1, "-lavfi", "fps=15,scale=320:-1:flags=lanczos [x]; [x][1:v] paletteuse", "-y", pathout};
        execFFmpegBinary(complexCommand, "palettegen");
    }

    public void excuteImagetoGif(String pathin, String pathout, String size, int delayed) {
        String[] complexCommand = {"-f", "image2", "-framerate", delayed + "", "-i", pathin, "-vf", "scale=" + size, pathout};
        execFFmpegBinary(complexCommand, "gif");
    }

    //
    public void executeCutVideoCommand(int startMs, int endMs, String pathin, String pathout) {
        //String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs / 1000, "-t", "" + endMs / 1000, dest.getAbsolutePath()};
        String[] complexCommand = {"-ss", "" + startMs / 1000, "-y", "-i", pathin, "-t", "" + (endMs - startMs) / 1000, "-vcodec",
                "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", pathout};
        execFFmpegBinary(complexCommand, "cutvideo");

    }

    //
//    public void executeConcatVideoCommand(String pathin, String pathout) {
//        //String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs / 1000, "-t", "" + endMs / 1000, dest.getAbsolutePath()};
//        String list = generateList(new String[]{pathin, pathin});
//        String[] complexCommand = {"ffmpeg",
//                "-f",
//                "concat",
//                "-i",
//                list,
//                "-c",
//                "copy",
//                pathout};
//        execFFmpegBinary(complexCommand);
//
//    }
//
//    private static String generateList(String[] inputs) {
//        File list;
//        Writer writer = null;
//        try {
//            list = File.createTempFile("ffmpeg-list", ".txt");
//            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list)));
//            for (String input : inputs) {
//                writer.write("file '" + input + "'\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "/";
//        } finally {
//            try {
//                if (writer != null)
//                    writer.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return list.getAbsolutePath();
//    }
    public void addCurveintoVideo(String pathvideo, String pathcurve, String pathoutvideo) {
        String[] complexCommand = {"-y", "-i", pathvideo,
                "-strict", "experimental", "-vf", "curves=psfile=" + pathcurve, "-b", "2097k", "-vcodec",
                "mpeg4", "-ab", "48000", "-ac", "2", "-ar", "22050", pathoutvideo};
        execFFmpegBinary(complexCommand, "curve");
    }

    public void extracGifFromVideo(String pathvideo, String pathout) {
        String[] complexCommand = {"-y", "-i", pathvideo, "-strict", "experimental", "-r", 20 + "", pathout};
        execFFmpegBinary(complexCommand, "curve");
    }
}
