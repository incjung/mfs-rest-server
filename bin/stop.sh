#!/bin/bash
if readlink -f "$0" > /dev/null 2>&1
then
    CLIBIN=`readlink -f "$0"`
else
    CLIBIN="$0"
fi

_bin=`dirname "$CLIBIN"`
_bin=`cd "${_bin}"; pwd`

PID=mfs-rest-server.pid

if [ -f $pid ];
then
    kill -9 `cat $PID`
fi
