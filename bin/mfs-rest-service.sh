#!/bin/bash
if readlink -f "$0" > /dev/null 2>&1
then
    CLIBIN=`readlink -f "$0"`
else
    CLIBIN="$0"
fi

_bin=`dirname "$CLIBIN"`
_bin=`cd "${_bin}"; pwd`

export PORT=8000

CLI_HOME=`cd ${_bin} ; cd .. ; pwd`
echo $CLI_HOME

PID=/opt/mapr/mfs-rest-server/bin/mfs-rest-server.pid

case $1 in
  start)
     echo "Starting Service"
     if [ ! -f $PID ]; then
         nohup java -jar $CLI_HOME/target/mfs-rest-server.jar  2>> /dev/null >> /dev/null &
         echo $! > $PID
     else
         echo "$SERVICE_NAME is already running ..."
     fi
  ;;
  stop)
     if [ -f $PID ]; then
        pid=$(cat $PID);
        echo "Service stoping ..."
        kill $pid;
        echo "Service stopped ..."
        rm $PID
     else
        echo "Service is not running ..."
     fi
  ;;
  status)
     if [ -f $PID ]; then
        echo "Service running ..."
        exit 0
     else
        echo "Service is not running ..."
        exit 1
     fi
  ;;
esac
