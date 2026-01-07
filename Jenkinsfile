pipeline {
    agent any
    stages {
        stage ('code quality') {
                    steps {
                        bat './gradlew sonar'
                        archiveArtifacts 'sonar/libs/*.jar'
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
