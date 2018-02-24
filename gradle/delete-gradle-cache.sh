#!/bin/bash
# As in https://stackoverflow.com/a/40748841:
id=$1
if [ -z "$id" ]; then
  echo "Please provide an group or artifact id to delete"
else
  find ~/.gradle/caches/ -type d -name "$id" -prune -exec rm -rf "{}" \; -print
fi
