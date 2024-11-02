pipeline {
    agent any

    stages {
        // Étape pour configurer Git
        stage('Setup Git Config') {
            steps {
                sh 'git config --global http.postBuffer 524288000'
            }
        }

        // Étape pour récupérer le code de GitHub
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ibtihel'
                    withCredentials([usernamePassword(credentialsId: '608aa325-9043-49fb-afb2-6e953ec29495', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '608aa325-9043-49fb-afb2-6e953ec29495']]
                        ])
                    }
                }
            }
        }

        // Étape pour nettoyer et compiler avec Maven
        stage('Maven Clean and Compile') {
            steps {
                script {
                    sh 'mvn clean compile'
                }
            }
        }

        // Étape pour empaqueter l'application dans un JAR avec Maven
        stage('Maven Package') {
            steps {
                script {
                    sh 'mvn package'
                }
            }
        }

        // Étape pour analyser le code avec SonarQube
        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: '608aa325-9043-49fb-afb2-6e953ec29495', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://localhost:9001 \
                          -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }
    }
}