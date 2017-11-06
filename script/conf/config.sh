#!/bin/sh

TEST_ENV=1

HEICHA_ACTIVITY_BASE_DIR=/home/work/heicha-activity
HEICHA_ACTIVITY_TMP_DIR=$HEICHA_ACTIVITY_BASE_DIR/tmp

HEICHA_ACTIVITY_MYSQL_BIN=/home/work/local/bin/mysql
HEICHA_ACTIVITY_MYSQL_HOST=10.211.6.47
HEICHA_ACTIVITY_MYSQL_PORT=3306
HEICHA_ACTIVITY_MYSQL_USER=heicha_admin
HEICHA_ACTIVITY_MYSQL_PWD=o4TJtIpwSJM=
HEICHA_ACTIVITY_MYSQL_DB=heicha_db
if [ "$TEST_ENV" == "1" ]
then
    HEICHA_ACTIVITY_MYSQL_HOST=st01-sw-gaccqa1.st01
    HEICHA_ACTIVITY_MYSQL_PORT=3310
    HEICHA_ACTIVITY_MYSQL_USER=wacc
    HEICHA_ACTIVITY_MYSQL_PWD=123456
fi


ACTIVITY_LOG=$HEICHA_ACTIVITY_BASE_DIR/log/stat_query_log
function LOG_FATAL() {
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] FATAL $0 $1" >> $ACTIVITY_LOG
}
function LOG_ERROR() {
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] ERROR $0 $1" >> $ACTIVITY_LOG
}
function LOG_WARN() {
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] WARN $0 $1" >> $ACTIVITY_LOG
}
function LOG_INFO() {
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] INFO $0 $1" >> $ACTIVITY_LOG
}
function LOG_DEBUG() {
    echo "[`date "+%Y-%m-%d %H:%M:%S"`] DEBUG $0 $1" >> $ACTIVITY_LOG
}




