pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                bat './gradlew'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}
