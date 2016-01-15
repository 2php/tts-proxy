#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <dirent.h>
#include <unistd.h>
#include <sys/stat.h>
#include <time.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/time.h>
#include <dlfcn.h>
#include "iFly_TTS.h"
#include "TTSErrcode.h"
#include "jni.h"
#include "com_tinet_ttssc_TtsJni.h"


#define MAX_IP_LEN 20
#define MAX_FILE_PATH_LEN 256
#define MAX_TEXT_LEN 1024

#define TTSS_NUM 10
#define TTS_NAME_LEN 128
#define TTS_ADDR_LEN 128
#define TTS_PORT_LEN 128

FILE *g_log_fd = NULL;
static char *g_lib_name = "libiflytts.so";
//function export by lib
void *g_lib_handle = NULL;
Proc_TTSInitializeEx g_TTSInitializeEx = NULL;
Proc_TTSUninitialize g_TTSUninitialize = NULL;
Proc_TTSConnect g_TTSConnect = NULL;
Proc_TTSDisconnect g_TTSDisconnect = NULL;
Proc_TTSSynthText2File g_TTSSynthText2File = NULL;
Proc_TTSSynthTextEx g_TTSSynthTextEx = NULL;
Proc_TTSSynthText g_TTSSynthText = NULL;
Proc_TTSFetchNext g_TTSFetchNext = NULL;
Proc_TTSSetParam g_TTSSetParam = NULL;
Proc_TTSGetParam g_TTSGetParam = NULL;
static char *g_wav_ext = ".wav";
char g_cur_txt[MAX_TEXT_LEN];
int g_cur_txt_len;
static char *g_log_file = "/var/log/ttsc/ttsc.log";

struct  log4deb
{
        char uuid[256];
        char fname[256];  //读取文件的名 格式为  ： UID:MD5 截取下来分别存，为了log
        char ipaddr[32];
        int flen; // 文件长度
        int f_time;
        struct timeval read_time; // 读文件的时刻
        struct timeval connect_start_time; // 开始连接的时刻
        struct timeval connect_result_time; // 连接有结果的时刻
        struct timeval sync_start_time; // 发送合成请求的时刻
        struct timeval sync_result_time; // 合成有结果返回的时刻
        struct timeval disconnect_start_time; // 发起断开请求的时刻
        struct timeval disconnect_result_time; // 发起断开请求的时刻

        char connect_status[64]; // 连接的状态
        char sync_status[64];  // 合成的状态
        char disconnect_status[64]; // 断开请求的状态

};

static struct log4deb logdeb; // for debug log


void load_tts_lib(const char* lib_name)
{
	fprintf(g_log_fd, "loading lib...\n");
	fflush(g_log_fd);
	if(g_lib_handle == NULL){
		g_lib_handle = (void *)dlopen(lib_name, RTLD_LAZY);//RTLD_LAZY | RTLD_NOW
		if (!g_lib_handle) {
			printf ("fail to load lib %s:%s\n", lib_name, dlerror());
			fflush(g_log_fd);
			exit(1);
		}
	}
	fprintf(g_log_fd, "loading function...\n");
	fflush(g_log_fd);
	g_TTSInitializeEx = dlsym(g_lib_handle, "TTSInitializeEx");
	if(g_TTSInitializeEx == NULL){
		fprintf(g_log_fd, "function TTSInitializeEx not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSUninitialize = dlsym(g_lib_handle, "TTSUninitialize");
	if(g_TTSUninitialize == NULL){
		fprintf(g_log_fd, "function TTSUninitialize not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSConnect = dlsym(g_lib_handle, "TTSConnect");
	if(g_TTSConnect == NULL){
		fprintf(g_log_fd, "function TTSConnect not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSDisconnect = dlsym(g_lib_handle, "TTSDisconnect");
	if(g_TTSDisconnect == NULL){
		fprintf(g_log_fd, "function TTSDisconnect not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSSynthText2File = dlsym(g_lib_handle, "TTSSynthText2File");
	if(g_TTSSynthText2File == NULL){
		fprintf(g_log_fd, "function TTSSynthText2File not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSSynthTextEx = dlsym(g_lib_handle, "TTSSynthTextEx");
	if(g_TTSSynthTextEx == NULL){
		fprintf(g_log_fd, "function TTSSynthTextEx not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSSynthText = dlsym(g_lib_handle, "TTSSynthText");
	if(g_TTSSynthText == NULL){
		fprintf(g_log_fd, "function TTSSynthText not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSFetchNext = dlsym(g_lib_handle, "TTSFetchNext");
	if(g_TTSFetchNext == NULL){
		fprintf(g_log_fd, "function TTSFetchNext not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSSetParam = dlsym(g_lib_handle, "TTSSetParam");
	if(g_TTSSetParam == NULL){
		fprintf(g_log_fd, "function TTSSetParam not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	g_TTSGetParam = dlsym(g_lib_handle, "TTSGetParam");
	if(g_TTSGetParam == NULL){
		fprintf(g_log_fd, "function TTSGetParam not found.\n");
		fflush(g_log_fd);
		exit(1);
	}
	return;
}

JNIEXPORT jint JNICALL Java_com_tinet_ttssc_TtsJni_Initialize
  (JNIEnv *env, jobject object){
    g_log_fd = fopen(g_log_file, "a+");
    load_tts_lib(g_lib_name);
	fprintf(g_log_fd, "Initializing iFlyTTS system...\n");
    int ret;
	if ((ret = g_TTSInitializeEx("intp60", NULL)) != TTSERR_OK)
	{
		if (ret == TTSERR_NOLICENCE)
		{			
			fprintf(g_log_fd, "Error in initializing TTS system, have no licence to run this application.\n");
			fflush(g_log_fd);
			return 1;
		}
		else
		{
			fprintf(g_log_fd, "Error in initializing TTS system, Error Code %d\n", ret); 
			fflush(g_log_fd);
			return 1;				
		}
	}
	fprintf(g_log_fd, "TTS system Initilized.\n");
	fflush(g_log_fd);
    return 0;
}

/*
 * Class:     com_tinet_ttssc_TtsJni
 * Method:    Uninitialize
 * Signature: ()V
 */
JNIEXPORT jint JNICALL Java_com_tinet_ttssc_TtsJni_Uninitialize
  (JNIEnv *env, jobject object){
   return g_TTSUninitialize();
}

/*
 * Class:     com_tinet_ttssc_TtsJni
 * Method:    request
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_tinet_ttssc_TtsJni_request
  (JNIEnv *env, jobject object, jstring text, jstring wav_file, jstring jip, jint jspeed, jint jvid, jint jvolume){
	const char* c_text = (*env)->GetStringUTFChars(env, text, NULL );
	const char* c_wav_file = (*env)->GetStringUTFChars(env, wav_file, NULL );
	const char* server_ip = (*env)->GetStringUTFChars(env, jip, NULL );
	int speed = jspeed;
	int language = jvid;
	int volume = jvolume;
	//----------------------------------------
	// Connect to TTS Service
	//----------------------------------------
	TTSConnectStruct tts_connect;
	HTTSINSTANCE instance;
	memset(&tts_connect, 0, sizeof(TTSConnectStruct));
	const char *company_name = "ti-net";// developer information
	const char *user_name	= "ti-net";
	const char *product_name	= "ttsc";
	const char *serial_num	= "*****-*****-*****"; // Enter your product serial number here!
	int res=0;
	int ret;
	int fmt = TTS_AHF_STAND;


	tts_connect.dwSDKVersion = IFLYTTS_SDK_VER;
	strcpy(tts_connect.szCompanyName, company_name);
	strcpy(tts_connect.szUserName, user_name);
	strcpy(tts_connect.szProductName, product_name);
	strcpy(tts_connect.szSerialNumber, serial_num);
	strcpy(tts_connect.szServiceUID, "intp60");
	tts_connect.bSetParams = TRUE;
	tts_connect.nCodePage = TTS_CP_UTF8;//TTS_CP_UTF8;


	strncpy(tts_connect.szTTSServerIP, server_ip, sizeof(tts_connect.szTTSServerIP)-1);
	gettimeofday(&logdeb.connect_start_time,NULL);
	instance = g_TTSConnect(&tts_connect);
	gettimeofday(&logdeb.connect_result_time,NULL);
	snprintf(logdeb.connect_status,sizeof(logdeb.connect_status)-1,"%ld",tts_connect.dwErrorCode);
	if(instance == NULL){
		if (tts_connect.dwErrorCode == TTSERR_INVALIDSN){				
				fprintf(g_log_fd, "Invalid serial number!\n");
				fflush(g_log_fd);
				res=-4;
				return res;
			}else{				
				fprintf(g_log_fd, "Error in Connect to TTS server %s,error code=%ld \n", server_ip, tts_connect.dwErrorCode);
				fflush(g_log_fd);
				res=-1;
				return res;
			}
	}

	fprintf(g_log_fd, "Connect to TTS server %s successfully.\n", server_ip);
	fflush(g_log_fd);
	

	ret = g_TTSSetParam(instance, TTS_PARAM_AUDIOHEADFMT, &fmt, sizeof(int));
	if(ret != TTSERR_OK){
		fprintf(g_log_fd, "error when disconnect code=%d\n", ret);
		fflush(g_log_fd);
	}
	fprintf(g_log_fd, "set auido format=%d.\n", fmt);
	fflush(g_log_fd);

	ret = g_TTSSetParam(instance, TTS_PARAM_VOLUME, &volume, sizeof(int));
	if(ret != TTSERR_OK){
			fprintf(g_log_fd, "error when set volume value code=%d\n", ret);
			fflush(g_log_fd);
	}
	fprintf(g_log_fd, "set volume value=%d\n",volume);
	fflush(g_log_fd);
	if(language != 1){ 
		if(language == 2){
			language = 3;
		}
		ret = g_TTSSetParam(instance, TTS_PARAM_VID, &language, sizeof(int));
		if(ret != TTSERR_OK){
			fprintf(g_log_fd, "error when set language value code=%d\n", ret);
			fflush(g_log_fd);
		}
		fprintf(g_log_fd, "set language value=%d\n",language);
		fflush(g_log_fd);
	}
	ret = g_TTSSetParam(instance, TTS_PARAM_SPEED, &speed, sizeof(int));
	if(ret != TTSERR_OK){
			fprintf(g_log_fd, "error when set speed value code=%d\n", ret);
			fflush(g_log_fd);
	}
	fprintf(g_log_fd, "set speed value=%d\n",speed);
	fflush(g_log_fd);

	TTSData	tts_data;
	//---------------------------------------
	// Synthesize text to audio data
	//---------------------------------------
	memset(&tts_data, 0, sizeof(tts_data));
	tts_data.dwInBufSize = strlen(c_text);
	tts_data.szInBuf = c_text;

	gettimeofday(&logdeb.sync_start_time,NULL);
	fprintf(g_log_fd, "start synthText2File %s %ld %ld\n", c_wav_file,logdeb.sync_start_time.tv_sec, logdeb.sync_start_time.tv_usec);
    fflush(g_log_fd);
	ret = g_TTSSynthText2File(instance, &tts_data, c_wav_file, NULL, FALSE, NULL);
	gettimeofday(&logdeb.sync_result_time,NULL);
	snprintf(logdeb.sync_status,sizeof(logdeb.sync_status)-1,"%d",ret);
	fprintf(g_log_fd, "finish synthText2file %s %ld %ld\n", c_wav_file,logdeb.sync_result_time.tv_sec, logdeb.sync_result_time.tv_usec);
 	fflush(g_log_fd);

	if(ret != TTSERR_OK){
		fprintf(g_log_fd, "error when synthtext code=%d\n", ret);
		fflush(g_log_fd);
		res=-2;
	}
	//------------------------------------------------------------------------------
	// Disconnect from TTS service,call TTSDisconnect
	//------------------------------------------------------------------------------
	gettimeofday(&logdeb.disconnect_start_time,NULL);
	ret = g_TTSDisconnect(instance);
	gettimeofday(&logdeb.disconnect_result_time,NULL);
	snprintf(logdeb.disconnect_status,sizeof(logdeb.disconnect_status)-1,"%d",ret);
	if(ret != TTSERR_OK){
		fprintf(g_log_fd, "error when disconnect code=%d\n", ret);
		fflush(g_log_fd);
		res=-3;
	}
	return res;

}
