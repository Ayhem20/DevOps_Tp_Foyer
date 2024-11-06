pipeline {
    agent any
    environment {
        NEXUS_URL = 'http://localhost:8081/repository/maven-releases/tn/esprit/tp-foyer/5.0.4/tp-foyer-5.0.4.jar' // The Nexus URL for the JAR file
        JAR_FILE = 'tp-foyer-5.0.4.jar' // The name of the JAR file to download
    }

    stages {

        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ayhem'
                    withCredentials([usernamePassword(credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "*/${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de']]
                        ])
                    }
                }
            }
        }

        stage('Maven Clean and Compile and test') {
            steps {
                script {
                    sh 'mvn clean compile test'
                }
            }
        }
        
        stage('Maven Package') {
            steps {
                script {
                    sh 'mvn package'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '71c996e3-a2eb-47ae-b081-f6d349669f6d', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://192.168.33.10:9000 \
                          -Dsonar.login=$SONAR_USER \
                          -Dsonar.password=$SONAR_PASSWORD
                        '''
                    }
                }
            }
        }
        
        stage('Maven Deploy') {
            steps {
                script {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }

        stage('Download JAR from Nexus') {
            steps {
                script {
                    echo "Downloading JAR from Nexus"
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials-id', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh "wget --user=$NEXUS_USER --password=$NEXUS_PASSWORD -O ${JAR_FILE} ${NEXUS_URL}"
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile
                    sh "docker build -t ayhem42/tp-foyer:latest ."
                }
            }
        }
        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'a742dcf0-5d07-4e6f-9d98-7e8833da1070', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        // Login to DockerHub
                        sh "echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin"

                        // Push the image to DockerHub
                        sh "docker push ayhem42/tp-foyer:latest"
                    }
                }
            }
        }
        stage('Docker Compose') {
            steps {
                script {
                    echo "Running Docker Compose"
                    sh 'docker compose up -d'
                    //sh 'docker compose down'
                }
            }
        }
    }
} 