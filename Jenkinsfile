pipeline {
    agent any
    stages {
        stage ('test') {
            steps{
                bat './gradlew  test jacocoTestReport'
                junit 'build/test-results/test/*.xml'
                cucumber buildStatus: 'UNSTABLE',
                                    fileIncludePattern: '**/*.json',
                                    jsonReportDirectory: 'build/reports/cucumber'
            }
        }
        stage ('code analysis') {
            tools {
                jdk 'JDK-11'
            }
            steps {
                bat './gradlew sonar'
            }
        }
        stage ('build') {
            steps {
                bat './gradlew build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
