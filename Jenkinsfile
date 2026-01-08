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
                             jsonReportDirectory: 'build/reports/cucumber'
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
                    bat './gradlew publish -PmavenRepoUrl=%MAVEN_REPO_URL% -PmavenUser=%MAVEN_USER% -PmavenPass=%MAVEN_PASS%'
                    }
                }
            }

            stage('notification') {
                steps {
                    script {
                        emailext(
                            subject: "Pipeline réussi: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                            body: """
                                <html>
                                    <body>
                                        <h2>Pipeline exécuté avec succès!</h2>
                                        <p><strong>Projet:</strong> ${env.JOB_NAME}</p>
                                        <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                                        <p><strong>Branche:</strong> ${env.BRANCH_NAME}</p>
                                        <p><strong>Statut:</strong> SUCCESS </p>
                                        <p><strong>Durée:</strong> ${currentBuild.durationString}</p>
                                        <hr>
                                        <p>Le déploiement a été effectué avec succès sur MyMavenRepo.</p>
                                        <p><a href="${env.BUILD_URL}">Voir les détails du build</a></p>
                                    </body>
                                </html>
                            """,
                            mimeType: 'text/html',
                            to: 'mellitimalik81@gmail.com',
                            from: "%GMAIL_USER%"
                        )
                        echo "Envoi de la notification Slack..."
                        bat "curl -X POST -H "Content-type: application/json" --data "{\\"text\\":\\"Deployment réussi! Projet: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\\"}" ${SLACK_WEBHOOK}"
                        echo "Notifications envoyées avec succès!"
                        }
                    }
                }
            }
        }

    post {
        failure {
            script {
                echo "Pipeline échoué - Envoi des notifications d'échec"
                emailext(
                    subject: "Pipeline échoué: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """
                        <html>
                             <body>
                                <h2 style="color: red;">Pipeline échoué!</h2>
                                <p><strong>Projet:</strong> ${env.JOB_NAME}</p>
                                <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                                <p><strong>Branche:</strong> ${env.BRANCH_NAME}</p>
                                <p><strong>Statut:</strong> FAILED</p>
                                <p><strong>Durée:</strong> ${currentBuild.durationString}</p>
                                <hr>
                                <p>Une erreur s'est produite lors de l'exécution du pipeline.</p>
                                <p><a href="${env.BUILD_URL}console">Voir les logs du build</a></p>
                             </body>
                        </html>
                    """,
                    mimeType: 'text/html',
                    to: 'mellitimalik81@gmail.com',
                    from: "%GMAIL_USER%"
                )

                bat "curl -X POST -H \"Content-type: application/json\" --data \"{\\"text\\":\\"Pipeline échoué! Projet: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}\\"}\" ${SLACK_WEBHOOK}"
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
