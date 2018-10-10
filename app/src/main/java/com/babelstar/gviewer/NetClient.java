package com.babelstar.gviewer;
@SuppressWarnings("ALL")
public class NetClient {
	public static final int NET_SUCCESS = 0;
	public static final int NET_ERR_RUNNING	= 8;	//Business process being executed
	
	public static final int GPS_CTRL_TYPE_FLIP_NORMAL 	= 16;	//Forward Flip
	public static final int GPS_CTRL_TYPE_FLIP_REVERSE 	= 17;	//Reverse Flip
	
	public static final int SEARCH_FINISHED = 99;
	public static final int SEARCH_FAILED = 100;
	public static final int SEARCH_DEFAULT_PORT = 6688;

	public static final int GPS_PTZ_MOVE_LEFT	= 0;
	public static final int GPS_PTZ_MOVE_RIGHT	= 1;
	public static final int GPS_PTZ_MOVE_TOP	= 2;
	public static final int GPS_PTZ_MOVE_BOTTOM	= 3;
	public static final int GPS_PTZ_MOVE_LEFT_TOP	= 4;
	public static final int GPS_PTZ_MOVE_RIGHT_TOP	= 5;
	public static final int GPS_PTZ_MOVE_LEFT_BOTTOM	= 6;
	public static final int GPS_PTZ_MOVE_RIGHT_BOTTOM	= 7;

	public static final int GPS_PTZ_FOCUS_DEL =	8;
	public static final int GPS_PTZ_FOCUS_ADD =	9;
	public static final int GPS_PTZ_LIGHT_DEL = 10;
	public static final int GPS_PTZ_LIGHT_ADD = 11;
	public static final int GPS_PTZ_ZOOM_DEL = 12;
	public static final int GPS_PTZ_ZOOM_ADD = 13;
	public static final int GPS_PTZ_LIGHT_OPEN	= 14;
	public static final int GPS_PTZ_LIGHT_CLOSE	= 15;
	public static final int GPS_PTZ_WIPER_OPEN	= 16;
	public static final int GPS_PTZ_WIPER_CLOSE	= 17;
	public static final int GPS_PTZ_CRUISE	= 18;
	public static final int GPS_PTZ_MOVE_STOP = 19;
	
	public static final int GPS_PTZ_SPEED_DEFAULT = 128;
	
	public static final int GPS_RGB_FORMAT_565 = 1;
	public static final int GPS_RGB_FORMAT_888 = 2;
	public static final int GPS_RGB_FORMAT_8888	= 3;
	
	//Network Type
	public static final int GPS_NET_TYPE_3G		= 0;
	public static final int GPS_NET_TYPE_WIFI	= 1;
	public static final int GPS_NET_TYPE_NET	= 2;

	public static final int GPS_FILE_LOCATION_DEVICE	= 1;		//Device
	public static final int GPS_FILE_LOCATION_STOSVR	= 2;		//Storage server
	//public static final int GPS_FILE_LOCATION_LOCAL		= 3;	//Client
	public static final int GPS_FILE_LOCATION_DOWNSVR	= 4;		//Auto download server

	public static final int GPS_FILE_ATTRIBUTE_JPEG		= 1;
	public static final int GPS_FILE_ATTRIBUTE_RECORD	= 2;
	///#define GPS_FILE_ATTRIBUTE_ALL				3		//
	public static final int GPS_FILE_ATTRIBUTE_LOG		= 4;	
	
	public static final int GPS_CHANNEL_ALL				= 99;	//All Channel
	
	public static final int GPS_FILE_TYPE_NORMAL		= 0;
	public static final int GPS_FILE_TYPE_ALARM			= 1;
	public static final int GPS_FILE_TYPE_ALL			= -1;
	
	public static final int GPS_STREAM_MODE_FILE		= 1;	//文件模式
	public static final int GPS_STREAM_MODE_REAL		= 2;	//实时模式
	public static final int GPS_STREAM_MODE_STREAM		= 3;	//流模式

	public native static void Initialize();
	public native static void UnInitialize();
	public native static long SetDirSvr(String svrIP, String strLanIP, int port, int lanFirst);

	public native static long OpenRealPlay(String devIdno, int nChn, int nMode, int nCntMode);
	public native static int SetAutoSelect(long lRealHandle, int autoSelect);
	public native static int SetRealSession(long lRealHandle, String session);
	public native static int SetRealAddress(long lRealHandle, String ip, int port);
	public native static int StartRealPlay(long lRealHandle);
	public native static int GetRPlayStatus(long lRealHandle);
	public native static int SetStreamMode(long lRealHandle, int nMode);
	//rgbLength: rgb565 Buffer Length
	//size[0] = width
	//size[1] = height
	//When the function returns 0, need to determine the size [0] and size [1] is zero, if not zero, it may be long enough
	public native static int GetRPlayImage(long lRealHandle, int rgbLength, byte[] rgb565, int[] size, int nRgbFormat);
	//format: The array length is 3, respectively nChannels, nSamplePerSec, nBitPerSample
	public native static int GetWavFormat(long lRealHandle, int[] format);
	public native static int GetWavData(long lRealHandle, byte[] pWavBuf, int nWavLen);
	//Obtain rate
	public native static int GetRPlayRate(long lRealHandle);
	public native static int CaptureBMP(long lRealHandle, String fileName);
	public native static int RPlayPtzCtrl(long lRealHandle, int nCommand, int nSpeed, int nParam);
	public native static int RPlayStartRecord(long lRealHandle, String recPath, String devName);
	public native static int RPlayStopRecord(long lRealHandle);
	public native static int RPlayPlaySound(long lRealHandle, int isPlay);
	public native static int StopRealPlay(long lRealHandle);
	public native static int CloseRealPlay(long lRealHandle);

	//Intercom
	public native static long TBOpenTalkback(String devIdno, int nChn, int nCntMode);
	public native static int TBSetRealServer(long lTalkbackHandle, String ip, int usPort, String szSession);
	public native static int TBSetSession(long lTalkbackHandle, String session);
	public native static int TBStartTalkback(long lTalkbackHandle);
	public native static int TBStopTalkback(long lTalkbackHandle);
	public native static int TBGetStatus(long lTalkbackHandle);
	public native static int TBSendWavData(long lTalkbackHandle, byte[] pWavBuf, int nWavLen);
	public native static int TBGetWavFormat(long lTalkbackHandle, int[] format);
	public native static int TBGetWavData(long lTalkbackHandle, byte[] pWavBuf, int nWavLen);
	public native static int TBCloseTalkback(long lTalkbackHandle);

	//Video file search
	public native static long SFOpenSrchFile(String devIdno, int nLocation, int nAttributenFile);
	public native static int SFSetRealServer(long lSearchHandle, String ip, int usPort, String szSession);
	public native static int SFSetSession(long lSearchHandle, String session);
	//szFileInfo:	szFile[256]:nYear:nMonth:nDay:uiBegintime:uiEndtime:szDevIDNO:nChn:nFileLen:nFileType:nLocation:nSvrID
	public native static int SFGetSearchFile(long lSearchHandle, byte[] szFileInfo, int nLength);
	public native static int SFStartSearchFile(long lSearchHandle, int nYear, int nMonth, int nDay
			, int nRecType, int nChn, int nBegTime, int nEndTime);
	public native static int SFStopSearchFile(long lSearchHandle);
	public native static int SFCloseSearchFile(long lSearchHandle);

	//Remote Playback Interface PB = PlayBack
	public native static long PBOpenPlayBack(String tmpPath);
	public native static int PBSetRealServer(long lPlaybackHandle, String ip, int usPort, String szSession);
	public native static int PBSetSession(long lSearchHandle, String session);
	public native static int PBStartPlayback(long lPlaybackHandle, String fileInfo, int nPlayChannel, int nBegMinSecond, int nEndMinSecond
			, int bPlayOnlyIFrame);
	//please use PBStartPlaybackEx
	public native static int PBStartPlaybackEx(long lPlaybackHandle, byte[] fileInfo, int nLength, int nPlayChannel, int nBegMinSecond, int nEndMinSecond
			, int bPlayOnlyIFrame);
	//NETMEDIA_API int	API_CALL	NETMEDIA_PBAdjustedWndRect(long lPlaybackHandle);
	public native static int PBStopPlayback(long lPlaybackHandle);
	public native static int PBGetRPlayStatus(long lRealHandle);
	//rgbLength: rgb565 buffer length
	//size[0] = width
	//size[1] = height
	//When the function returns 0, need to determine the size [0] and size [1] is zero, if not zero, it may be long enough
	public native static int PBGetRPlayImage(long lRealHandle, int rgbLength, byte[] rgb565, int[] size, int nRgbFormat);
	public native static int PBGetWavData(long lRealHandle, byte[] pWavBuf, int nWavLen);
	//Take file download rate, unit KByte
	public native static int PBGetFlowRate(long lPlaybackHandle);
	//Pause
	public native static int PBPause(long lPlaybackHandle, int bPause);
	//Drag
	public native static int PBSetPlayTime(long lPlaybackHandle, int nMinSecond);
	//Only Play I Frame
	public native static int PBSetPlayIFrame(long lPlaybackHandle, int bIFrame);
	//Get the number of milliseconds to play
	public native static int PBGetPlayTime(long lPlaybackHandle);
	//Get download milliseconds
	public native static int PBGetDownTime(long lPlaybackHandle);
	//Image capture
	public native static int PBCaptureBMP(long lPlaybackHandle, String bmpFile);
	//Whether play finished
	public native static int PBIsPlayFinished(long lPlaybackHandle);
	//Whether download finished
	public native static int PBIsDownFinished(long lPlaybackHandle);
	//Close remote playback
	public native static int PBClosePlayback(long lPlaybackHandle);

	//Search Device
	public native static long SDOpenSearch();
	//szDevInfo为DevIDNO:NetType(0=Wifi, 1=Net):IP:DevType:chn
	public native static int SDGetSearchResult(long lSearchHandle, byte[] szDevIdno, int nLength);
	public native static int SDStartSearch(long lSearchHandle, String ip, int usPort);
	public native static int SDStopSearch(long lSearchHandle);
	public native static int SDCloseSearch(long lSearchHandle);
	public native static int SDConfigWifi(int usPort, String user, String pwd, String devIdno, String wifiSsid, String wifiPwd);

	//Send control commands to the device
	public native static long MCOpenControl(String session, int bAutoSelect, int uiTimeoutMinSecond);
	//Forward and reverse interfaces
	public native static int MCSendCtrl(long lControl, String devIdno, int nCtrlType);
	public native static int MCGetResult(long lControl, byte[] result, int nLength);
	public native static int MCCloseControl(long lControl);

	//local file play
	public native static long RECOpenFile(String file);
	public native static int RECCloseFile(long lHandle);
	public native static int RECGetFileInfo(long lHandle, byte[] fileInfo, int nLength);
	public native static int RECPlay(long lHandle);
	public native static int RECStop(long lHandle);
	public native static int RECPause(long lHandle, int bPause);
	public native static int RECOneByOne(long lHandle);
	public native static int RECSetPlayRate(long lHandle, int Rate);
	public native static long RECGetPlayTime(long lHandle);
	public native static int RECSetPlayTime(long lHandle, long nMinSecond);
	public native static int RECIsPlayEnd(long lHandle);

	public native static int RECGetPlayImage(long lHandle, int rgbLength, byte[] rgb, int[] size, int rgbFormat);
	public native static int RECGetWavFormat(long lHandle, byte[] format);
	public native static int RECGetWavData(long lHandle, byte[] pWavBuf, int nWavLen);
	public native static int RECPlaySound(long lHandle, int bOpen);
	public native static int RECCaptureBMP(long lHandle, String BMPFile);

	static {
		System.loadLibrary("ttxclient");
	}
}
