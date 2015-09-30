package j.wu.media.mp3;

import java.io.Serializable;


public class Mp3Info implements Serializable{

	private String baiduMusicId;
	private static final long serialVersionUID = 1L;
	private String id;
	private String songName;//音乐标题
	private long duration;//时长
	private String singerName;//艺术家
	private long fileSize;//文件大小
	private String filePath;//文件路径
    private String facePicPath;//封面的图片路径
    private boolean isFav; //是否是我喜欢的
    private String fileName; //文件名
    private String specialName; //专辑名称
    private String year; //年份
    private String bitRate; //比特率
	private String format; // 格式
	private String encodingType; //编码类型
	private String channels;//声道
	private String sampleRate; //采样率
	private String numberOfFrames; //帧数
	private String sortLetters; //拼音的大写字母
	private boolean isPlaying;
	private boolean isCheck;

	@Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Mp3Info [id=");
        buffer.append(id);
        buffer.append(", songName=");
        buffer.append(songName);
        buffer.append(", duration=");
        buffer.append(duration);
        buffer.append(", singerName=");
        buffer.append(singerName);
        buffer.append(", fileSize=");
        buffer.append(fileSize);
        buffer.append(", filePath=");
        buffer.append(filePath);
        buffer.append(", facePicPath=");
        buffer.append(facePicPath);
        buffer.append(", isFav=");
        buffer.append(isFav);
        buffer.append(", fileName=");
        buffer.append(fileName);
        buffer.append(", specialName=");
        buffer.append(specialName);
        buffer.append(", year=");
        buffer.append(year);
        buffer.append(", bitRate=");
        buffer.append(bitRate);
        buffer.append(", format=");
        buffer.append(format);
        buffer.append(", encodingType=");
        buffer.append(encodingType);
        buffer.append(", channels=");
        buffer.append(channels);
        buffer.append(", sampleRate=");
        buffer.append(sampleRate);
        buffer.append(", numberOfFrames=");
        buffer.append(numberOfFrames);
        buffer.append("]");
		return buffer.toString();
	}

	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getBitRate() {
		return bitRate;
	}
	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	public String getChannels() {
		return channels;
	}
	public void setChannels(String channels) {
		this.channels = channels;
	}
	public String getSampleRate() {
		return sampleRate;
	}
	public void setSampleRate(String sampleRate) {
		this.sampleRate = sampleRate;
	}
	public String getNumberOfFrames() {
		return numberOfFrames;
	}
	public void setNumberOfFrames(String numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id == null ? "" : id;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String artist) {
		this.singerName = artist;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long size) {
		this.fileSize = size;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String url) {
	    this.filePath = url == null ? "" : url;
	}
	public String getFacePicPath() {
		return facePicPath;
	}
	public void setFacePicPath(String facePicPath) {
		this.facePicPath = facePicPath;
	}
	public boolean isFav() {
		return isFav;
	}
	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName == null ? "" : fileName;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getBaiduMusicId() {
		return baiduMusicId;
	}
	public void setBaiduMusicId(String baiduMusicId) {
		this.baiduMusicId = baiduMusicId;
	}

	public Object getDateSource() {
		return getFilePath();
	}
}
