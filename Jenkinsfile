pipeline {
    agent any
    stages {
        stage ('test') {
            steps{
                bat './gradlew  test'
                archiveArtifacts 'test/reports/tests/test/index.html'
                bat './gradlew generateCucumberReports'
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
