# CATALINA_OPTS="$CATALINA_OPTS -Dfile.encoding=UTF-8 -Djava.locale.providers=JRE,COMPAT,CLDR -Djava.net.preferIPv4Stack=true -Duser.timezone=GMT -Xms2560m -Xmx2560m -XX:MaxNewSize=1536m -XX:MaxMetaspaceSize=768m -XX:MetaspaceSize=768m -XX:NewSize=1536m -XX:SurvivorRatio=7"
GLOWROOT_ENABLED="true"
CATALINA_OPTS="$CATALINA_OPTS -Dfile.encoding=UTF-8 -Djava.locale.providers=JRE,COMPAT,CLDR -Djava.net.preferIPv4Stack=true -Duser.timezone=GMT -Xms4g -Xmx4g -XX:MaxNewSize=1536m -XX:MaxMetaspaceSize=768m -XX:MetaspaceSize=768m -XX:NewSize=1536m -XX:SurvivorRatio=7"

JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/sun.net.www.protocol.http=ALL-UNNAMED --add-opens=java.base/sun.net.www.protocol.https=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED --add-opens=jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED"

if [ "$1" = "glowroot" ]
then
	GLOWROOT_OPTS="-javaagent:${CATALINA_HOME}/../glowroot/glowroot.jar"

	CATALINA_OPTS="${CATALINA_OPTS} ${GLOWROOT_OPTS}"

	shift
fi

# if [ "$GLOWROOT_ENABLED" = "true" ]
# then
	GLOWROOT_OPTS="-javaagent:${CATALINA_HOME}/../glowroot/glowroot.jar"

	CATALINA_OPTS="${CATALINA_OPTS} ${GLOWROOT_OPTS}"
# fi

