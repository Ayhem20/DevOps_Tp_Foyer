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
                sh 'mvn clean compile'
            }
        }

        // Étape pour empaqueter l'application dans un JAR avec Maven
        stage('Maven Package') {
            steps {
                sh 'mvn package'
            }
        }

        // Étape pour analyser le code avec SonarQube
        stage('MVN SONARQUBE') {
            steps {
                withCredentials([string(credentialsId: 'c27ceaee-5193-4a7c-988b-22150e0c3e9d', variable: 'SONAR_TOKEN')]) {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }

        // Étape pour déployer sur Nexus
        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'a278b6b7-0941-4825-a111-0459663e9bc5', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }

        // Étape pour construire l'image Docker
        stage('Building Image') {
            steps {
                script {
                    // Construire l'image Docker
                    sh 'docker build -t ibtihelgr/alpine:latest .'
                }
            }
        }

        // Étape pour déployer l'image Docker sur DockerHub
        stage('Deploy Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: '57d779b6-7114-4142-977c-9b93fff27676', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                        docker push ibtihelgr/alpine:latest
                    '''
                }
            }
        }
    }
}