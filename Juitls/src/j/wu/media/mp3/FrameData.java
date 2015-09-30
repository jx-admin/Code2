package j.wu.media.mp3;

public class FrameData {
	private FrameHead frameHead;
	private byte[] bitData;
	private int bitSize;

	public FrameData(byte[] head) throws Exception {
		this.frameHead = new FrameData.FrameHead(head);
		this.bitSize = this.frameHead.getBitDataSize();
	}

	public void setBitData(byte[] data) {
		this.bitData = data;
	}

	public int getBitSize() {
		return this.bitSize;
	}

	class FrameHead {
		/* 4字节(32位)数据 */
		/**
		 * <pre>
		 * AAAAAAAA AAABBCCD EEEEFFGH IIJJKLMM 
		 * 注意以上格式：将解释按照位操作数据
		 *  A 31-21 [11bit] * Frame sync帧同步 
		 *  B 20-19 [2 bit] MPEG音频版本 (00 - MPEG Version 2.5) (01 - * 保留) (10 - MPEG Version 2) (11 - MPEG Version 1) 
		 *  C 18-17 [2 bit] 层描述 * (00 - 保留) (01 - Layer III) (10 - Layer II) (11 - Layer I) 
		 *  D 16 [1 * bit] 保护位 0意味着受CRC保护，帧头后面跟16位的CRC 
		 *  E 15-12 [4 bit] 比特率 
		 *  F 11-10 [2 bit] * 采样频率 bits MPEG1 MPEG2 MPEG2.5 00 44100 22050 11025 01 48000 24000 * 12000 10 32000 16000 8000 11 reserv reserv reserv 
		 *  G 9 [1 bit] * 意味着帧里包含padding位，仅当采样频率为44.1KHz时发生 
		 *  H 8 [1 bit] 私有未知位 
		 *  I 7-6 [2 bit] * 声道模式 (00 - 立体声) (01 - 联合立体声) (11 - 双通道) (11 - 单通道) 
		 *  J 5-4 [2 bit] 扩展模式 * bit 强度立体声 MS立体声 00 关闭 关闭 01 开 关 10 关闭 开 11 开 开 
		 *  K 3 [1 bit] 版权保护位 
		 *  L 2 * [1 bit] 是否拷贝 0=副本 1=原来的
		 *  M 1-0 [2 bit] 重点位 (00 - 不重视) (01 - 50/15毫秒) * (10 - 保留) (11 - 国际电报电话咨询委员会J.17)
		 */
		private String MPEGVersion;// MPEG版本
		private boolean protectionBit;// true 代表别保护 帧头后有16bit CRC
		private int bitrate;// 比特率
		private int sampling;// 采样率
		private boolean paddingBit;//
		private String channelMode;// 声道模式
		private int bitDataSize;// 实际声音数据大小(字节)

		public FrameHead(byte[] head) throws Exception {
			if (head.length != 4) {
				System.out.println("FrameHead数据长度不足4字节！");
				throw new Exception();
			}
			this.MPEGVersion = this.getMPEGVersionByBit(head[1]);
			this.protectionBit = this.getProtectionBitByBit(head[1]);
			this.bitrate = this.getBitrateByBit(head[2]);
			this.sampling = this.getSamplingByBit(head[2]);
			this.paddingBit = this.getPaddingBitByBit(head[2]);
			this.channelMode = this.getChannelModeByBit(head[3]);
			this.bitDataSize = this.getBitDataSizeByBit();

		}

		public int getBitDataSize() {
			return bitDataSize;
		}

		public int getBitrate() {
			return bitrate;
		}

		public String getChannelMode() {
			return channelMode;
		}

		public String getMPEGVersion() {
			return MPEGVersion;
		}

		public boolean isPaddingBit() {
			return paddingBit;
		}

		public boolean isProtectionBit() {
			return protectionBit;
		}

		public int getSampling() {
			return sampling;
		}

		// 所有的位操作在private方法进行
		private String getMPEGVersionByBit(byte b) {
			b = (byte) ((b & 24) >>> 3);// b&00011000
			if (b == 0)
				return "MPEG2.5";
			else if (b == 1)
				return "保留";
			else if (b == 2)
				return "MPEG2";
			else if (b == 3)
				return "MPEG1";
			else
				return "ERROR";
		}

		private boolean getProtectionBitByBit(byte b) {
			b = (byte) (b & 1);// b&00000001
			if (b == 0)
				return true;
			else
				return false;
		}

		private int getBitrateByBit(byte b) {
			/*
			 * 0000 free free free free free free 0001 32 32 32 32 32 8 (8) 0010
			 * 64 48 40 64 48 16 (16) 0011 96 56 48 96 56 24 (24) 0100 128 64 56
			 * 128 64 32 (32) 0101 160 80 64 160 80 64 (40) 0110 192 96 80 192
			 * 96 80 (48) 0111 224 112 96 224 112 56 (56) 1000 256 128 112 256
			 * 128 64 (64) 1001 288 160 128 288 160 128 (80) 1010 320 192 160
			 * 320 192 160 (96) 1011 352 224 192 352 224 112 (112) 1100 384 256
			 * 224 384 256 128 (128) 1101 416 320 256 416 320 256 (144) 1110 448
			 * 384 320 448 384 320 (160) 1111 bad bad bad bad bad bad
			 */
			b = (byte) ((b & 240) >>> 4);// b&11110000
			if (b == 0) {
				return 0;
			} else if (b == 1)
				return 8000;
			else if (b == 2)
				return 16000;
			else if (b == 3)
				return 24000;
			else if (b == 4)
				return 32000;
			else if (b == 5)
				return 40000;
			else if (b == 6)
				return 48000;
			else if (b == 7)
				return 56000;
			else if (b == 8)
				return 64000;
			else if (b == 9)
				return 80000;
			else if (b == 10)
				return 96000;
			else if (b == 11)
				return 112000;
			else if (b == 12)
				return 128000;
			else if (b == 13)
				return 144000;
			else if (b == 14)
				return 160000;
			else if (b == 15)
				return -1;
			else
				return -1;

		}

		private int getSamplingByBit(byte b) {
			/*
			 * bits MPEG1 MPEG2 MPEG2.5 00 44100 22050 11025 01 48000 24000
			 * 12000 10 32000 16000 8000 11 reserv reserv reserv
			 */
			b = (byte) ((b & 12) >>> 2);// 00001100
			if (b == 0) {
				if (this.MPEGVersion.equals("MPEG1"))
					return 44100;
				else if (this.MPEGVersion.equals("MPEG2"))
					return 22050;
				else if (this.MPEGVersion.equals("MPEG2.5"))
					return 11025;
				else
					return -1;
			} else if (b == 1) {
				if (this.MPEGVersion.equals("MPEG1"))
					return 48000;
				else if (this.MPEGVersion.equals("MPEG2"))
					return 24000;
				else if (this.MPEGVersion.equals("MPEG2.5"))
					return 12000;
				else
					return -1;
			} else if (b == 2) {
				if (this.MPEGVersion.equals("MPEG1"))
					return 32000;
				else if (this.MPEGVersion.equals("MPEG2"))
					return 16000;
				else if (this.MPEGVersion.equals("MPEG2.5"))
					return 8000;
				else
					return -1;
			} else
				return -1;
		}

		private boolean getPaddingBitByBit(byte b) {
			b = (byte) ((b & 2) >>> 1);// 00000010
			if (b == 1 && this.sampling == 44100)
				return true;// ??
			else
				return false;
		}

		private String getChannelModeByBit(byte b) {
			/* (00 - 立体声) (01 - 联合立体声) (11 - 双通道) (11 - 单通道) */
			b = (byte) ((b & 192) >>> 6);// 11000000
			if (b == 0)
				return "立体声";
			else if (b == 1)
				return "联合立体声";
			else if (b == 2)
				return "双声道";
			else if (b == 3)
				return "单声道";
			else
				return "ERROR";
		}

		private int getBitDataSizeByBit() {
			// (((MpegVersion == MPEG1 ? 144 : 72) * Bitrate) / SamplingRate) +
			// PaddingBit

			int size = (int) ((this.MPEGVersion.equals("MPEG1") ? 144 : 72)
					* this.bitrate / this.sampling);
			if (this.paddingBit)
				return size + 1;
			else
				return size;
		}

	}// inner Class

}// outer Class
