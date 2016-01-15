#!/bin/bash

file=$1
TTSSC_URL=http://internal-vocp-ttssc-internal-8477452.cn-north-1.elb.amazonaws.com.cn/interface/ttsGet

if [[ "${file}" == "" ]]; then
	echo "usage: tts_pre_build.sh pre_build_file(csv)"
	exit
fi

while read line
do 
	for str in `echo ${line//,/ }`;
	do 
		if [ "${str}" != "" ]; then
			curl_result=`curl ${TTSSC_URL} -m 10 -s -L -d "text=${str}&priority=9&uniqueId=tts-pre-build&redirect=0"`
			echo $str result: $curl_result
		fi
	done;
done < ${file}

