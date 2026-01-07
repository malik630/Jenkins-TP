pipeline {
    agent any
    stages {
        stage ('code quality') {
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
