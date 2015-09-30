package j.wu.media.mp3;

public interface PublicInfo {

	/*
	char Header[3];     //标签头必须是"TAG"否则认为没有标签 
	char Title[30];     //标题 
	char Artist[30];    //作者 
	char Album[30];     //专集 
	char Year[4];       //出品年代 
	char Comment[28];  //备注 
	char reserve;       //保留 
	char track;        //音轨 
	char Genre;         //类型 
	*/
	public String getTitle();
	public String getArtist();
	public String getAlbum();
	public String getYaer();
	public String getComment();
	public String getGenre();
	}
