#
# simple makefile for hexml
# doesn't understand .java -> .class dependencies, 'cause javac is fast enough.
#

export BASE_DIR = $(PWD)
export SRC_BASE = $(BASE_DIR)/src
export SRC_DIR = $(SRC_BASE)/org/webcycle/hexml
export DEMO_DIR = $(BASE_DIR)/demo
export CLASSES_DIR = $(BASE_DIR)/classes

hexml:
	make -C $(SRC_DIR)

all:	hexml docs jar

docs:
	javadoc -classpath $(CLASSES_DIR) -sourcepath $(SRC_BASE) -windowtitle hexml -overview $(SRC_DIR)/hexml.html -d docs org.webcycle.hexml CA.carleton.freenet.ak117.util

jar:
	jar cf hexml.jar classes/*


