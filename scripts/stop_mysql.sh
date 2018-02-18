#!/bin/bash
if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  echo Running Linux
  service mysql stop
elif [ "$(uname)" == "Darwin" ]; then
  sudo /usr/local/mysql/support-files/mysql.server stop
fi