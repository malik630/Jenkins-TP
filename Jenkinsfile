pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                bat './gradlew build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
        stage ('code quality') {
            steps {
                bat './gradlew sonar'
                archiveArtifacts 'sonar/libs/*.jar'
            }
        }
    }
}
