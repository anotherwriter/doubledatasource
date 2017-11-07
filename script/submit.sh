#!/bin/sh

WORKDIR=`dirname $0`
WORK_DIR=`cd $WORKDIR&&pwd`
CONFIG_DIR=$WORK_DIR/conf
source $CONFIG_DIR/config.sh


$HEICHA_ACTIVITY_MYSQL_BIN -h$HEICHA_ACTIVITY_MYSQL_HOST -P$HEICHA_ACTIVITY_MYSQL_PORT -u$HEICHA_ACTIVITY_MYSQL_USER -p$HEICHA_ACTIVITY_MYSQL_PWD -Dheicha_db -NB -e "select id, md5, phone, charge_status from charging_user where charge_status=0 limit 100" > $HEICHA_ACTIVITY_TMP_DIR/new_work

key="94d3e85b26dce7a0ec292da44ad3ac75"
partyId=50002014
userName="baiduwangxun"


failCount=0
succCount=0

function parse_json(){
#echo "$1" | sed "s/.*\"$2\":\([^,}]*\).*/\1/"
echo "${1//\"/}" | sed "s/.*$2:\([^,}]*\).*/\1/"
}

function rand(){
    min=$1
    max=$(($2-$min+1))
    num=$(date +%s%N)
    echo $(($num%$max+$min))
}
randomNum=$(rand 1 9999)

while read ID MD5 PHONE CHARGE_STATUS
do
    LOG_INFO "start processing ID=$ID md5=$MD5 phone=$PHONE status=$CHARGE_STATUS"

    randomNum=`expr $randomNum + 1`

    if [ $randomNum -gt 9999 ]
    then
        randomNum=1
    fi

    randomNum=`printf "%04d" $randomNum`

    timeNow=`date +%Y%m%d%H%M%S%N`;
    timeStamp=${timeNow:0:17}

    requestid="$timeStamp$partyId$randomNum"
    body="\"body\":{\"userdataList\":[{\"userPackage\":\"30\",\"mobiles\":\"$PHONE\"}],\"size\":1,\"type\":\"0\",\"requestid\":\"$requestid\"}}"
    sign=`echo -n "body${body}key${key}partyId${partyId}requestid${requestid}"|md5sum|cut -d ' ' -f1`

    reqData="{\"header\":{\"sign\":\"$sign\",\"partyId\":$partyId},$body";

    resultStr=`curl --request POST --url http://localhost:8080/api/activity/HandleSubmit/submit --header 'cache-control: no-cache' --header 'content-type: application/x-www-form-urlencoded' --data param="$reqData"`
    LOG_DEBUG "$resultStr"
    if [ $? -ne 0 ]
    then
        # write to error log
        echo "curl error"
        echo "$PHONE" >> $HEICHA_ACTIVITY_ERROR_DIR/curl_error
        continue
    fi

    echo "resultStr=$resultStr"

    code=$(parse_json $resultStr "code")
    description=$(parse_json $resultStr "description")
    sendid=$(parse_json $resultStr "sendid")
    requestid=$(parse_json $resultStr "requestid")
    echo "code:$code description:$description sendid:$sendid requestid:$requestid"

    if [ "$code" = "0" ]
    then
        succCount=`expr $succCount + 1`
        LOG_INFO "rsp from operator ok: phone=$PHONE code=$code description=$description sendid=$sendid requestid=$requestid"
        $HEICHA_ACTIVITY_MYSQL_BIN -h$HEICHA_ACTIVITY_MYSQL_HOST -P$HEICHA_ACTIVITY_MYSQL_PORT -u$HEICHA_ACTIVITY_MYSQL_USER -p$HEICHA_ACTIVITY_MYSQL_PWD -Dheicha_db -NB -e "update charging_user set charge_status=1 where id=$ID limit 1"
    fi

    if [ "$code" != "0" ]
    then
        failCount=`expr $failCount + 1`
        LOG_ERROR "rsp from operator error: phone=$PHONE code=$code description=$description sendid=$sendid requestid=$requestid"
    fi
done <$HEICHA_ACTIVITY_TMP_DIR/new_work


echo "over"
taskNum=`wc -l $HEICHA_ACTIVITY_TMP_DIR/new_work`
LOG_INFO "finish $taskNum tasks succ: $succCount fail: $failCount"


