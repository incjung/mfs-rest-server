#!/bin/bash
if readlink -f "$0" > /dev/null 2>&1
then
    CLIBIN=`readlink -f "$0"`
else
    CLIBIN="$0"
fi

_bin=`dirname "$CLIBIN"`
_bin=`cd "${_bin}"; pwd`

CLI_HOME=`cd ${_bin} ; cd .. ; pwd`

RUN_CMD="java -jar ../target/mfs-rest-server.jar"

PID=mfs-rest-server.pid

if [ -f $pid ];
then
    if kill -0 `cat $PID`; then
        rm -f $PID
    fi

    echo RUN_CMD
    $RUN_CMD &
    echo $! > $PID
    
fi
