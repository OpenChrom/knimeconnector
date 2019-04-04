pipeline {
    agent any
    tools { 
        jdk 'JDK8' 
        maven 'MAVEN3'
    }
    triggers {
        pollSCM('H/5 * * * *')
        upstream(upstreamProjects: "compilations/openchrom-community/${BRANCH_NAME}", threshold: hudson.model.Result.SUCCESS)
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
		stage('deploy') {
			when { branch 'develop' }
		    steps {
		        withCredentials([string(credentialsId: 'DEPLOY_HOST', variable: 'DEPLOY_HOST')]) {
					sh 'scp -r knimeconnector/openchrom/sites/net.openchrom.xxd.process.supplier.knime.updateSite/target/repository/* '+"${DEPLOY_HOST}knime/3.3.2/repository"
				}
			 withCredentials([string(credentialsId: 'DEPLOY_HOST', variable: 'DEPLOY_HOST')]) {
					sh 'scp -r knimeconnector/openchrom/sites/net.openchrom.knimeconnector.updateSite/target/repository/* '+"${DEPLOY_HOST}knimeconnector/repository"
				}
		    }
		}
    }
    post {
    	always {
    	    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
    	    warnings canRunOnFailed: true, consoleParsers: [[parserName: 'Maven']], shouldDetectModules: true
    	    openTasks canRunOnFailed: true, ignoreCase: true, high: 'FIXME', low: 'XXX', normal: 'TODO', pattern: '**/*.java', shouldDetectModules: true
    	}
        failure {
            emailext(body: '${DEFAULT_CONTENT}', mimeType: 'text/html',
		         replyTo: '$DEFAULT_REPLYTO', subject: '${DEFAULT_SUBJECT}',
		         to: emailextrecipients([[$class: 'CulpritsRecipientProvider'],
		                                 [$class: 'RequesterRecipientProvider']]))
        }
        success {
            cleanWs notFailBuild: true
        }

    }
}