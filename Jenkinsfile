pipeline {
    agent any

    tools {
        jdk 'JDK-21'
    }

    environment {
        MAVEN_REPO_URL = credentials('MAVEN_REPO_URL')
        MAVEN_USER = credentials('MAVEN_USER')
        MAVEN_PASS = credentials('MAVEN_PASS')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        SLACK_WEBHOOK = credentials('SLACK_WEBHOOK')
        GMAIL_USER = credentials('GMAIL_USER')
        GMAIL_PASS = credentials('GMAIL_PASS')
    }

    stages {
        stage ('test') {
            steps{
                script{
                    bat './gradlew  test jacocoTestReport'
                    junit 'build/test-results/test/*.xml'
                    cucumber buildStatus: 'UNSTABLE',
                             fileIncludePattern: '**/*.json',
                             jsonReportDirectory: 'reports'
                }
            }
        }

        stage ('code analysis') {
            tools {
                jdk 'JDK-11'
            }
            steps {
                script {
                    withSonarQubeEnv('sonar') {
                        bat './gradlew sonar -Dsonar.projectKey=TP7 -Dsonar.projectName=TP7 -Dsonar.host.url=http://localhost:9000 -Dsonar.token=%SONAR_TOKEN%'
                    }
                }
            }
        }

        /*stage('Code Quality') {
            steps {
                script {
                    echo "code quality"
                    echo "Vérification du Quality Gate..."

                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Le Quality Gate a échoué: ${qg.status}"
                        }
                        echo "Quality Gate validé avec succès!"
                    }
                }
            }
        }*/

        stage ('build') {
            tools {
                jdk 'JDK-21'
            }

            steps {
                script {
                    bat './gradlew build'
                    bat './gradlew javadoc'
                    archiveArtifacts 'build/libs/*.jar'
                    archiveArtifacts artifacts: 'build/docs/**/*'
                }
            }
        }

        stage('deploy') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'MAVEN_REPO_URL', variable: 'MAVEN_REPO_URL'),
                        string(credentialsId: 'MAVEN_USER', variable: 'MAVEN_USER'),
                        string(credentialsId: 'MAVEN_PASS', variable: 'MAVEN_PASS')
                    ]) {
                        bat './gradlew publish'
                    }
                }
            }
        }

        stage('notification') {
            steps {
                script {
                    mail to: 'mellitimalik81@gmail.com',
                         subject: "Pipeline réussi: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                         body: """
Pipeline exécuté avec succès!

Projet: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Branche: ${env.BRANCH_NAME}
Statut: SUCCESS
Durée: ${currentBuild.durationString}

Le déploiement a été effectué avec succès sur MyMavenRepo.

Voir les détails du build: ${env.BUILD_URL}
                         """

                    echo "Envoi de la notification Slack..."
                    bat 'curl -X POST -H "Content-type: application/json" --data "{\\"text\\":\\"Pipeline reussi! Projet: ' + env.JOB_NAME + ' - Build #' + env.BUILD_NUMBER + ' - Branche: ' + env.BRANCH_NAME + '\\"}" ' + SLACK_WEBHOOK
                    echo "Notifications envoyées avec succès!"
                }
            }
        }
    }

    post {
        failure {
            script {
                echo "Pipeline échoué - Envoi des notifications d'échec"
                mail to: 'mellitimalik81@gmail.com',
                     subject: "Pipeline échoué: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                     body: """
Pipeline échoué!

Projet: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Branche: ${env.BRANCH_NAME}
Statut: FAILED
Durée: ${currentBuild.durationString}

Une erreur s'est produite lors de l'exécution du pipeline.

Voir les logs du build: ${env.BUILD_URL}console
                     """

                bat 'curl -X POST -H "Content-type: application/json" --data "{\\"text\\":\\"Pipeline echoue! Projet: ' + env.JOB_NAME + ' - Build #' + env.BUILD_NUMBER + ' - Branche: ' + env.BRANCH_NAME + '\\"}" ' + SLACK_WEBHOOK
            }
        }

        success {
            echo "Pipeline terminé avec succès!"
        }

        always {
            echo "Nettoyage de l'espace de travail..."
            cleanWs()
        }
    }
}