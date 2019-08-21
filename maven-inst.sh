# Artifacts that need to be installed manually
#==============================================
# com.jidesoft:jide-action:jar:3.4.7, 
# com.jidesoft:jide-common:jar:3.4.7, 
# com.jidesoft:jide-components:jar:3.4.7, 
# com.jidesoft:jide-dock:jar:3.4.7, 
# com.jidesoft:jide-grids:jar:3.4.7,
# com.google.gdata:com.google.gdata.spreadsheet:jar:3.0, 
# com.google.gdata:com.google.gdata.core:jar:1.0, 
# apache-beanutils:commons-beanutils:jar:1.7.0,
# com.googlecode.sardine:sardine:jar:314:
# 
# Make this file executable by running
# chmod a+x maven-inst.sh
#
#==============================================

mvn install:install-file -Dfile=lib/jide-action-3.4.7.jar -DgroupId=com.jidesoft -DartifactId=jide-action -Dversion=3.4.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jide-common-3.4.7.jar -DgroupId=com.jidesoft -DartifactId=jide-common -Dversion=3.4.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jide-components-3.4.7.jar -DgroupId=com.jidesoft -DartifactId=jide-components -Dversion=3.4.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jide-dock-3.4.7.jar -DgroupId=com.jidesoft -DartifactId=jide-dock -Dversion=3.4.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jide-grids-3.4.7.jar -DgroupId=com.jidesoft -DartifactId=jide-grids -Dversion=3.4.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/com.google.gdata.core-1.0.jar -DgroupId=com.google.gdata -DartifactId=com.google.gdata.core -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/com.google.gdata.spreadsheet-3.0.jar -DgroupId=com.google.gdata -DartifactId=com.google.gdata.spreadsheet -Dversion=3.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/commons-beanutils-1.7.0.jar -DgroupId=apache-beanutils -DartifactId=commons-beanutils -Dversion=1.7.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/sardine-314.jar -DgroupId=com.googlecode.sardine -DartifactId=sardine -Dversion=314 -Dpackaging=jar
