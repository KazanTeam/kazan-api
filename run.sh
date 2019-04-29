nohup java -Dfile.encoding=UTF-8 -Xmx2024m -classpath "lib/*" -Dlog4j.configuration=file:conf/log4j.properties -Dlog4j.debug org.d.api.Main > logs/stdout 2> logs/stderr < /dev/null &
PID=$!
echo $PID > logs/pid