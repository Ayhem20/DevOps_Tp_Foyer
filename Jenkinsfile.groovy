pipeline {
    agent any

    stages {
        // Stage to check out the code from GitHub
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'chaima-sassi'
                    withCredentials([usernamePassword(credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: 'c38cdad7-ffdc-4cf7-a254-95950ce5c532']]
                        ])
                    }
                }
            }
        }

        // Stage to clean and compile using Maven
        stage('Maven Clean and Compile') {
            steps {
                script {
                    sh 'mvn clean compile'
                }
            }
        }

        // Stage to package the application into a JAR
        stage('Maven Package') {
            steps {
                script {
                    sh 'mvn package'
                }
            }
        }

        // Stage to analyze code using SonarQube
        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: '0b0a7f3b-d5e8-40bc-8fd9-3a469de95804', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=DevOps_Tp_Foyer \
                          -Dsonar.host.url=http://192.168.56.4:9000 \
                          -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        // Stage to deploy the JAR file to Nexus
        stage('Deploy to Nexus') {
            steps {
                script {
                    def jarFile = 'target/tp-foyer-5.0.0.jar'
                    if (fileExists(jarFile)) {
                        withCredentials([usernamePassword(credentialsId: '6caa081d-c871-4a56-9fe4-a5b70bafaa0b', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh """
                              sh 'mvn clean deploy -DskipTests'
                            """
                        }
                    } else {
                        echo "Le fichier JAR ${jarFile} est introuvable. Déploiement annulé."
                    }
                }
            }
        }

        // Stage to build the Docker image
        stage('Building Image') {
            steps {
                script {
                    // Build the Docker image
                    sh 'docker build -t chaimasassi/tp-foyer:latest .'
                }
            }
        }

        // Stage to deploy the Docker image to DockerHub
        stage('Deploy Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '03be88ac-efe9-49b3-95ca-c4ac06b54c49', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                            docker push chaimasassi/tp-foyer:latest
                        '''
                    }
                }
            }
        }

        // Stage to run Docker Compose for deployment
        stage('Docker Compose') {
            steps {
                script {
                    echo "Running Docker Compose"
                    sh 'docker compose down'
                    sh 'docker compose up -d'
                }
            }
        }
    }

    // Post actions to handle pipeline completion and failures
    post {
        always {
            echo 'Pipeline completed.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
