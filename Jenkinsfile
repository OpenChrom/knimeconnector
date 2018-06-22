pipeline {
    agent any
    tools { 
        jdk 'JDK8' 
        maven 'MAVEN3'
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
    	stage('checkout') {
    	steps {
    			dir('knimeconnector') {
					checkout scm
				}
			}
    	}
		stage('build') {
			steps {
				sh 'mvn -B -Dmaven.repo.local=.repository -f knimeconnector/openchrom/cbi/knimeconnector.targetplatform/pom.xml install'
				sh 'mvn -B -Dmaven.repo.local=.repository -f knimeconnector/openchrom/cbi/net.openchrom.xxd.process.supplier.knime.cbi/pom.xml install'
			}
		}
    }
}