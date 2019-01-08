#!/bin/bash

SCRIPT_PATH=`readlink -f "$0"`;
cd `dirname ${SCRIPT_PATH}`
cd ..

CP=""
if [ -d target/classes ]; then
  CP=$CP:target/classes
fi

for jar in `/bin/ls -1 lib/*.jar`
do
  CP=$CP:$jar
done

  java -Xms256M -Xmx512M -cp $CP cn.enilu.tool.database.doc.generator.Main
