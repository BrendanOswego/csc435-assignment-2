#!/bin/bash
if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  service mysql start
elif [ "$(uname)" == "Darwin" ]; then
  sudo /usr/local/mysql/support-files/mysql.server start
fi