# The MIT License (MIT)
#
# Copyright (c) 2022 Che-Hung Lin
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

ARG BASE_CONTAINER=tomcat:9.0.58-jdk11-openjdk-buster
FROM $BASE_CONTAINER

ARG TOMCAT_USERNAME
ARG TOMCAT_PASSWORD

LABEL maintainer="Che-Hung Lin <edward730109@gmail.com>"

WORKDIR $CATALINA_HOME

RUN rm -rf webapps; \
    mv webapps.dist webapps; \
	sed -itmp "s#</tomcat-users>#<!--</tomcat-users>-->#g" ./conf/tomcat-users.xml; \
	echo "  <role rolename=\"manager-gui\"/>" >> ./conf/tomcat-users.xml; \
	echo "  <role rolename=\"manager-script\"/>" >> ./conf/tomcat-users.xml; \
	echo "  <user username=\"${TOMCAT_USERNAME}\" password=\"${TOMCAT_PASSWORD}\" roles=\"manager-gui\"/>" >> ./conf/tomcat-users.xml; \
	echo "  <user username=\"${TOMCAT_USERNAME}-text\" password=\"${TOMCAT_PASSWORD}\" roles=\"manager-script\"/>" >> ./conf/tomcat-users.xml; \
	echo "</tomcat-users>" >> ./conf/tomcat-users.xml; \
	sed -itmp "s#1|0:0:0:0:0:0:0:1#1|0:0:0:0:0:0:0:1|.*#g" ./webapps/manager/META-INF/context.xml; \
	sed -itmp "s#1|0:0:0:0:0:0:0:1#1|0:0:0:0:0:0:0:1|.*#g" ./webapps/host-manager/META-INF/context.xml;

COPY dirtools-web.war $CATALINA_HOME/webapps/dirtools.war

EXPOSE 8080
CMD ["catalina.sh", "run"]