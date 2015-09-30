package j.wu.media.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***
 * 最近在学习音乐媒体文件的数据结构。首先看的是流行的MP3格式，网上的资料也不少。但是看过后都不能深入理解结构。就抽空时间使用Java写了个简单的Tools
 * package
 * .当前只能解析MP3格式文件，而且还没有研究出声音压缩流解码的算法。以下代码主要功能：提取文件的标签头(PrivateInfo类)、扩展标签头
 * 、标签尾(PublicInfo类)、数据帧(FrameData类)。
 */
public class MP3 {
	private static Map<Integer, String> style = new HashMap<Integer, String>();

	private MP3.SongInfo songInfo;
	private MP3.DataInfo dataInfo;

	private List<FrameData> frameList;

	public MP3(String filename) throws Exception {
		File file = new File(filename);
		byte[] bufLast = new byte[128];// 结尾128字节
		byte[] bufFront = new byte[10];// 开头10字节
		byte[] bufFrame = null;// 中间数据帧
		byte[] headLabel = null;// 标签帧(未知长度和帧数)
		try {
			FileInputStream is = new FileInputStream(file);
			try {
				// head
				is.read(bufFront, 0, 10);
				this.dataInfo = new MP3.DataInfo(bufFront);

				// head frame
				headLabel = new byte[this.dataInfo.getSize()];
				is.read(headLabel, 0, headLabel.length);
				dataInfo.ID3ByHeadList(headLabel);
				// bufFrame
				bufFrame = new byte[(int) (file.length() - 128 - 10 - headLabel.length)];
				is.read(bufFrame, 0, bufFrame.length);
				this.analyseFrame(bufFrame);

				// v1
				is.skip(file.length()
						- (10 + headLabel.length + bufFrame.length));
				is.read(bufLast, 0, 128);
				this.songInfo = new MP3.SongInfo(bufLast);

			} catch (IOException e) {
				System.out.println("文件读取错误：" + file.getAbsolutePath()
						+ " 文件长度不足128。");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件路径：" + file.getAbsolutePath() + " 不存在。");
			// e.printStackTrace();
		}
	}

	static {
		/* Standard genre */
		style.put(0, "Blues");
		style.put(1, "ClassicRock");
		style.put(2, "Country");
		style.put(3, "Dance");
		style.put(4, "Disco");
		style.put(5, "Funk");
		style.put(6, "Grunge");
		style.put(7, "Hip-Hop");
		style.put(8, "Jazz");
		style.put(9, "Metal");
		style.put(10, "NewAge");
		style.put(11, "Oldies");
		style.put(12, "Other");
		style.put(13, "Pop");
		style.put(14, "R&B");
		style.put(15, "Rap");
		style.put(16, "Reggae");
		style.put(17, "Rock");
		style.put(18, "Techno");
		style.put(19, "Industrial");
		style.put(20, "Alternative");
		style.put(21, "Ska");
		style.put(22, "DeathMetal");
		style.put(23, "Pranks");
		style.put(24, "Soundtrack");
		style.put(25, "Euro-Techno");
		style.put(26, "Ambient");
		style.put(27, "Trip-Hop");
		style.put(28, "Vocal");
		style.put(29, "Jazz+Funk");
		style.put(30, "Fusion");
		style.put(31, "Trance");
		style.put(32, "Classical");
		style.put(33, "Instrumental");
		style.put(34, "Acid");
		style.put(35, "House");
		style.put(36, "Game");
		style.put(37, "SoundClip");
		style.put(38, "Gospel");
		style.put(39, "Noise");
		style.put(40, "AlternRock");
		style.put(41, "Bass");
		style.put(42, "Soul");
		style.put(43, "Punk");
		style.put(44, "Space");
		style.put(45, "Meditative");
		style.put(46, "InstrumentalPop");
		style.put(47, "InstrumentalRock");
		style.put(48, "Ethnic");
		style.put(49, "Gothic");
		style.put(50, "Darkwave");
		style.put(51, "Techno-Industrial");
		style.put(52, "Electronic");
		style.put(53, "Pop-Folk");
		style.put(54, "Eurodance");
		style.put(55, "Dream");
		style.put(56, "SouthernRock");
		style.put(57, "Comedy");
		style.put(58, "Cult");
		style.put(59, "Gangsta");
		style.put(60, "Top40");
		style.put(61, "ChristianRap");
		style.put(62, "Pop/Funk");
		style.put(63, "Jungle");
		style.put(64, "NativeAmerican");
		style.put(65, "Cabaret");
		style.put(66, "NewWave");
		style.put(67, "Psychadelic");
		style.put(68, "Rave");
		style.put(69, "Showtunes");
		style.put(70, "Trailer");
		style.put(71, "Lo-Fi");
		style.put(72, "Tribal");
		style.put(73, "AcidPunk");
		style.put(74, "AcidJazz");
		style.put(75, "Polka");
		style.put(76, "Retro");
		style.put(77, "Musical");
		style.put(78, "Rock&Roll");
		style.put(79, "HardRock");
		/* Extended genre */
		style.put(80, "Folk");
		style.put(81, "Folk-Rock");
		style.put(82, "NationalFolk");
		style.put(83, "Swing");
		style.put(84, "FastFusion");
		style.put(85, "Bebob");
		style.put(86, "Latin");
		style.put(87, "Revival");
		style.put(88, "Celtic");
		style.put(89, "Bluegrass");
		style.put(90, "Avantgarde");
		style.put(91, "GothicRock");
		style.put(92, "ProgessiveRock");
		style.put(93, "PsychedelicRock");
		style.put(94, "SymphonicRock");
		style.put(95, "SlowRock");
		style.put(96, "BigBand");
		style.put(97, "Chorus");
		style.put(98, "EasyListening");
		style.put(99, "Acoustic");
		style.put(100, "Humour");
		style.put(101, "Speech");
		style.put(102, "Chanson");
		style.put(103, "Opera");
		style.put(104, "ChamberMusic");
		style.put(105, "Sonata");
		style.put(106, "Symphony");
		style.put(107, "BootyBass");
		style.put(108, "Primus");
		style.put(109, "PornGroove");
		style.put(110, "Satire");
		style.put(111, "SlowJam");
		style.put(112, "Club");
		style.put(113, "Tango");
		style.put(114, "Samba");
		style.put(115, "Folklore");
		style.put(116, "Ballad");
		style.put(117, "PowerBallad");
		style.put(118, "RhythmicSoul");
		style.put(119, "Freestyle");
		style.put(120, "Duet");
		style.put(121, "PunkRock");
		style.put(122, "DrumSolo");
		style.put(123, "Acapella");
		style.put(124, "Euro-House");
		style.put(125, "DanceHall");
		style.put(126, "Goa");
		style.put(127, "Drum&Bass");
		style.put(128, "Club-House");
		style.put(129, "Hardcore");
		style.put(130, "Terror");
		style.put(131, "Indie");
		style.put(132, "BritPop");
		style.put(133, "Negerpunk");
		style.put(134, "PolskPunk");
		style.put(135, "Beat");
		style.put(136, "ChristianGangstaRap");
		style.put(137, "HeavyMetal");
		style.put(138, "BlackMetal");
		style.put(139, "Crossover");
		style.put(140, "ContemporaryChristian");
		style.put(141, "ChristianRock");
		style.put(142, "Merengue");
		style.put(143, "Salsa");
		style.put(144, "TrashMetal");
		style.put(145, "Anime");
		style.put(146, "JPop");
		style.put(147, "Synthpop");
	}

	class DataInfo implements PrivateInfo {
		/*
		 * char Header[3]; 字符串 "ID3"
		 * 
		 * char Ver; 版本号ID3V2.3 就记录3
		 * 
		 * char Revision; 副版本号此版本记录为0
		 * 
		 * char Flag; 存放标志的字节，这个版本只定义了三位，很少用到，可以忽略
		 * 
		 * char Size[4]; 标签大小，除了标签头的10 个字节的标签帧的大小
		 */
		private String id3;
		private int ver;
		private String revision;
		private String flag;
		private int size;

		public String toString() {
			String info = "id3: " + id3 + ", ver: " + ver + ", revision: "
					+ revision + ", flag: " + flag + ", zise: " + size;
			if (map != null) {
				info += map;
			}
			return info;
		}

		public DataInfo(byte[] data) throws Exception {
			byte[] temp = null;
			if (data.length != 10) {
				System.out.println("数据不足10字节或者大于10字节！");
				throw new Exception();
			}
			int pos = 0;
			temp = new byte[3];
			System.arraycopy(data, pos, temp, 0, 3);
			this.id3 = new String(temp);
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.ver = temp[0];
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.revision = new String(temp);
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.flag = new String(temp);
			pos = pos + temp.length;

			temp = new byte[4];
			System.arraycopy(data, pos, temp, 0, 4);
			this.size = MP3.getSizeByByte(temp);
			int total_size;

			this.size = (temp[0] & 0x7F) * 0x200000 + (temp[1] & 0x7F) * 0x400
					+ (temp[2] & 0x7F) * 0x80 + (temp[3] & 0x7F);
			int dataLeng = 0;
			dataLeng |= temp[0] & 0xff;
			dataLeng &= 0x000000ff;
			dataLeng <<= 8;
			dataLeng |= temp[1] & 0xff;
			dataLeng &= 0x0000ffff;
			dataLeng <<= 8;
			dataLeng |= temp[2] & 0xff;
			dataLeng &= 0x00ffffff;
			dataLeng <<= 8;
			dataLeng |= temp[3] & 0xff;
			this.size = dataLeng;

		}

		public int getSize() {
			return this.size;
		}

		Map<String, String> map = new HashMap<String, String>();

		public Map<String, String> ID3ByHeadList(byte[] buf) {
			System.out.println(buf.length);
			/*
			 * 标签帧： | 数据名 | 数据长度 | 标记 | [真实数据] | .....
			 */
			int pix = 0;
			byte[] head;// 4
			byte[] size;// 4
			byte[] flag;// 2
			int dataLeng = 0;
			byte[] dataBuf;// n

			for (; pix < buf.length;) {
				head = new byte[4];
				size = new byte[4];
				flag = new byte[2];
				System.arraycopy(buf, pix, head, 0, 4);
				pix = pix + 4;
				System.arraycopy(buf, pix, size, 0, 4);
				pix = pix + 4;
				System.arraycopy(buf, pix, flag, 0, 2);
				pix = pix + 2;
				// dataLeng = getSizeByByte(size);
				dataLeng = 0;
				dataLeng |= size[0] & 0xff;
				dataLeng &= 0x000000ff;
				dataLeng <<= 8;
				dataLeng |= size[1] & 0xff;
				dataLeng &= 0x0000ffff;
				dataLeng <<= 8;
				dataLeng |= size[2] & 0xff;
				dataLeng &= 0x00ffffff;
				dataLeng <<= 8;
				dataLeng |= size[3] & 0xff;

//				System.out.print("+++++++++" + dataLeng + " \t| ");
//				System.out.print(new String(head) + " \t| ");
				if (dataLeng <= 0) {
//					System.out.println("0 \t| end = " + pix);
					return map;
				}
				dataBuf = new byte[dataLeng];
				System.arraycopy(buf, pix, dataBuf, 0, dataLeng);

//				System.out.println(new String(dataBuf) + " \t| ");
				pix = pix + dataLeng;
				map.put(new String(head), new String(dataBuf));
				// System.out.println(new String(head) + " \t| " + new
				// String(dataBuf)
				// + " \t| " + pix);

			}

			return map;

		}
	}

	class SongInfo implements PublicInfo {
		private String tag;

		private String title;
		private String artist;
		private String album;
		private String year;
		private String comment;
		private String genre;

		private String reserve;
		private String track;

		public String toString() {
			return "tag: " + tag + ", title: " + title + ", artist: " + artist
					+ ", album: " + album + ", year: " + year + ", comment: "
					+ comment + ", genre: " + genre + ", reserve: " + reserve
					+ ", track: " + track;
		}

		public SongInfo(byte[] data) throws Exception {
			byte[] temp = null;
			if (data.length != 128) {
				System.out.println("数据不足128字节或者大于128字节！");
				throw new Exception();
			}
			int pos = 0;
			temp = new byte[3];
			System.arraycopy(data, pos, temp, 0, 3);
			this.tag = new String(temp);
			pos = pos + temp.length;

			if (!this.tag.equals("TAG")) {
				return;
			}

			temp = new byte[30];
			System.arraycopy(data, pos, temp, 0, 30);
			this.title = new String(temp);
			pos = pos + temp.length;

			temp = new byte[30];
			System.arraycopy(data, pos, temp, 0, 30);
			this.artist = new String(temp);
			pos = pos + temp.length;

			temp = new byte[30];
			System.arraycopy(data, pos, temp, 0, 30);
			this.album = new String(temp);
			pos = pos + temp.length;

			temp = new byte[4];
			System.arraycopy(data, pos, temp, 0, 4);
			this.year = new String(temp);
			pos = pos + temp.length;

			temp = new byte[28];
			System.arraycopy(data, pos, temp, 0, 28);
			this.comment = new String(temp);
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.reserve = new String(temp);
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.track = new String(temp);
			pos = pos + temp.length;

			temp = new byte[1];
			System.arraycopy(data, pos, temp, 0, 1);
			this.genre = style.get(new Byte(temp[0]).intValue());

		}

		public String getAlbum() {
			// TODO 自动生成方法存根
			return this.album;
		}

		public String getArtist() {
			// TODO 自动生成方法存根
			return this.artist;
		}

		public String getComment() {
			// TODO 自动生成方法存根
			return this.comment;
		}

		public String getGenre() {
			// TODO 自动生成方法存根
			return this.genre;
		}

		public String getTitle() {
			// TODO 自动生成方法存根
			return this.title;
		}

		public String getYaer() {
			// TODO 自动生成方法存根
			return this.year;
		}

	}

	private static int getSizeByByte(byte[] temp) {
		// return (int)(temp[0] & 0x7F) << 21| (int)(temp[1] & 0x7F) << 14|
		// (int)(temp[2] & 0x7F) << 7| (int)(temp[3] & 0x7F) + 10;
		int r = 1;
		if (temp[0] + temp[1] + temp[2] + temp[3] == 0)
			return 0;
		for (int x = 0; x < 4; x++)
			if (temp[x] != 0)
				r = r * temp[x];
		return r;
	}

	private void saveIcon(String file, byte date) {
		File facePath = new File(file);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(facePath);
			out.write(date);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void analyseFrame(byte[] frameByte) throws Exception {
		frameList = new LinkedList<FrameData>();
		int pix = 0;
		int size = 0;
		byte[] frameHead;// 帧头
		byte[] frameData;// 数据帧
		for (; pix < frameByte.length;) {
			frameHead = new byte[4];
			System.arraycopy(frameByte, pix, frameHead, 0, 4);
			pix = pix + 4;
			if (pix >= frameByte.length)
				break;

			FrameData frame = new FrameData(frameHead);
			size = frame.getBitSize();
			if (size <= 0)
				continue;
			// if(size<0) break;//是否注释
			frameData = new byte[size];
//			System.out.println("SIZE:" + size + "  获取源数组开始位置:" + pix
//					+ "  新数组大小:" + frameData.length + "  长度： "
//					+ frame.getBitSize() + "  总大小：" + frameByte.length
//					+ "  morehas:" + (frameByte.length - pix));
			int tmpLength = Math
					.min(frame.getBitSize(), frameByte.length - pix);
			System.arraycopy(frameByte, pix, frameData, 0, tmpLength);
			pix = pix + frame.getBitSize();
//			System.out.println("PIX:" + pix + "   SIZE:" + size);
			if (pix >= frameByte.length)
				break;
			frame.setBitData(frameData);
			this.frameList.add(frame);

			// if (pix>500) break;

		}
	}

	static public void main(String[] str) throws Exception {
		MP3 mp3 = new MP3(
				"C:/Users/Administrator.WIN-SF83G4UAHGD/Desktop/song/Ailee-Invitation-给你看 (보여줄게)(I'll Show You)-128.mp3");
		System.out.println(mp3.songInfo.getTitle());
		System.out.println(mp3.songInfo.getGenre());
		System.out.println(mp3.dataInfo.getSize());
		System.out.println(mp3.songInfo);
		 System.out.println(mp3.dataInfo);
	}

}
