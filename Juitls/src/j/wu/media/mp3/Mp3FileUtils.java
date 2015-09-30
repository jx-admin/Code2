package j.wu.media.mp3;

import j.wu.charset.CharSetUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

/**音乐文件信息解析
 * @author Administrator
 *
 */
public class Mp3FileUtils {
	public static void main(String[]args){
		for(File f:new File("E:/desktop/song/").listFiles()){
		Mp3Info mMp3Info=parseMp3(null,f);
		}
	}
	/**
	 * 音乐文件信息解析
	 * @param file
	 * @return
	 */
	public static Mp3Info parseMp3(Mp3Info mp3Info,File file) {
		if(isMp3File(file.getAbsolutePath())) {
			mp3Info = getHeadAndInfo(mp3Info,file);
        } else if(isCanPlayFile(file.getAbsolutePath())){
        	mp3Info = getOtherMusicInfo(mp3Info,file);
        }
		return mp3Info;
	}

	private static Mp3Info setMp3DefaultInfo(Mp3Info mp3,File file) {
		if(mp3==null){
			mp3 = new Mp3Info();
		}
		mp3.setFileName(file.getName());
		mp3.setId(UUID.randomUUID().toString());
		mp3.setFileSize(file.length());
		mp3.setFilePath(file.getAbsolutePath());
		mp3.setFav(false);
		return mp3;
	}

	private static boolean isMp3File(String file) {
		return file.toLowerCase().endsWith(".mp3");
	}

    private static boolean isCanPlayFile(String fileName) {
    	fileName=fileName.toLowerCase();
        return fileName.endsWith(".aac") || fileName.endsWith(".amr") || fileName.endsWith(".imy")
                || fileName.endsWith(".mid") || fileName.endsWith(".mpga")
                || fileName.endsWith(".wav") || fileName.endsWith(".xmf") || fileName.endsWith(".flac");
    }
    
    private static Mp3Info getOtherMusicInfo(Mp3Info info,File file) {
        info = setMp3DefaultInfo(info,file);
		/***比特率*/
		String bitRate = null;
		/***格式*/
		String format = null;
		String encodingType = null;
		/***声道*/
		String channels = null;
		/***采样率*/
		String sampleRate = null;
		/***帧数*/
		String numberOfFrames = null;
		/***歌曲名*/
		String songName = null;
		/***歌手*/
		String singerName = null;
		/***专辑*/
		String spcialName = null;
		/***发行年*/
		String year = null;
//		byte[] imageData = null;
		
        info.setBitRate(bitRate);
        info.setFormat(format);
        info.setEncodingType(encodingType);
        info.setChannels(channels);
        info.setSampleRate(sampleRate);
        info.setNumberOfFrames(numberOfFrames);
        String []splitSongName = file.getName().split("[.]");
        if(splitSongName.length > 1) {
            info.setSongName(splitSongName[0]);
        } else {
            info.setSongName(file.getName());
        }
        info.setSingerName(singerName);
        info.setSpecialName(spcialName);
        info.setYear(year);
        return info;
    }
	/**
	 * 获得头部信息
	 */
	private static Mp3Info getHeadAndInfo(Mp3Info mp3,File file) {
		Mp3Info info = setMp3DefaultInfo(mp3,file);
		/***比特率*/
		String bitRate = null;
		/***格式*/
		String format = null;
		String encodingType = null;
		/***声道*/
		String channels = null;
		/***采样率*/
		String sampleRate = null;
		/***帧数*/
		String numberOfFrames = null;
		/***歌曲名*/
		String songName = null;
		/***歌手*/
		String singerName = null;
		/***专辑*/
		String spcialName = null;
		/***发行年*/
		String year = null;
		byte[] imageData = null;
		try {
			MP3File mp3File = (MP3File) AudioFileIO.read(file);
			MP3AudioHeader header = mp3File.getMP3AudioHeader(); // mp3文件头部信息
			bitRate = header.getBitRate();
			format = header.getFormat();
			encodingType = header.getEncodingType();
			channels = header.getChannels();
			sampleRate = header.getSampleRate();
			numberOfFrames = header.getNumberOfFrames() + "";
			
			if (mp3File.hasID3v2Tag() || mp3File.hasID3v2Tag()) {
				Tag tag = mp3File.getTag();
				Artwork artwork = tag.getFirstArtwork(); // 获得第一张专辑图片
				if(artwork!=null){
					imageData = artwork.getBinaryData(); // 将读取到的专辑图片转成二进制
				}
				singerName = tag.getFirst(FieldKey.ARTIST);
				songName=tag.getFirst(FieldKey.TITLE);
				spcialName = tag.getFirst(FieldKey.ALBUM);
				year = tag.getFirst(FieldKey.YEAR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		info.setBitRate(bitRate);
		info.setFormat(format);
		info.setEncodingType(encodingType);
		info.setChannels(channels);
		info.setSampleRate(sampleRate);
		// 帧数
		info.setNumberOfFrames(numberOfFrames);
		//直接设置歌曲名
		if (null==songName||songName.length()==0) {
			songName = getFileNameNoEx(info.getFileName());
//			songName = mContext.getResources().getString(R.string.unknown_songs);
			info.setSongName(songName);
		}else{
			info.setSongName(CharSetUtils.charset(songName));
		}
		//歌手
		if(null==singerName||singerName.length()==0){
		}else{
			info.setSingerName(CharSetUtils.charset(singerName));
		}
		//将数组写入文件，保存图片
		if(null==info.getFacePicPath()||info.getFacePicPath().length()==0){
			if(imageData != null && imageData.length > 10){
				File facePath = new File("",info.getId()+".png");
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(facePath);
					out.write(imageData);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				info.setFacePicPath(facePath.getAbsolutePath());
			}else{
				String path = file.getAbsolutePath();
				StringBuffer sb = new StringBuffer(path);
				sb.delete(path.lastIndexOf("."), path.length());
				sb.append(".png");
				if(new File(sb.toString()).exists()){
					info.setFacePicPath(sb.toString());
				}
			}
		}
		//专辑
		if(null==spcialName||spcialName.length()==0){
		}else{
			spcialName=CharSetUtils.charset(spcialName);
			info.setSpecialName(spcialName);
		}
		//年代
		if(null==(year)||year.length()==0){
		}else{
			info.setYear(year);
		}
		return info;
	}

	/**
	 * 获得不包含扩展名的文件名
	 */
	private static String getFileNameNoEx(String filename){
		if(null==filename||filename.length()==0){
			return "";
		}
		int lastIndex=filename.lastIndexOf('.');
		if(lastIndex>0){
			return filename.substring(0, lastIndex);
		}
		return filename;
	}
	
}
