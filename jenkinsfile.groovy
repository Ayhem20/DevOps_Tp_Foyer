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
                    def branch = 'ibtihelreservation'
                    withCredentials([usernamePassword(credentialsId: 'b9570c36-2c1c-49c3-bf25-9dc5cdabf3e0', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: 'b9570c36-2c1c-49c3-bf25-9dc5cdabf3e0']]
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
        stage('MVN SONARQUBE') {
            steps {
                withCredentials([string(credentialsId: '24d5b7bd-64bc-4d5e-8b7c-9ae96e35ceec', variable: 'SONAR_TOKEN')]) {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }
        
        stage('Unit Tests - JUnit & Mockito') {  
            steps {  
                echo 'Running unit tests...'  
                sh 'mvn test'  
            }  
            post {  
                always {  
                    junit 'target/surefire-reports/*.xml' // Collect JUnit test results  
                }  
                failure {  
                    echo 'Unit tests failed!'  
                }  
            }  
        }  

        stage('JaCoCo Code Coverage') {
            steps {
                echo 'Running JaCoCo code coverage...'
                sh 'mvn jacoco:prepare-agent test jacoco:report'
            }
            post {
                always {
                    jacoco execPattern: 'target/jacoco.exec', 
                           classPattern: 'target/classes', 
                           sourcePattern: 'src/main/java', 
                           exclusionPattern: '/test/',
                           changeBuildStatus: true
                }
            }
        }

        // Étape pour déployer sur Nexus
        stage('Deploy to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '301661af-5b46-4e84-ba8f-709377baad5a', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh 'mvn clean deploy -DskipTests'
                    }
                }
            }
        }

        // Étape pour construire l'image Docker
        stage('Building Image') {
            steps {
                script {
                    sh 'docker build -t ibtihelgr/tp-foyer:latest .'
                }
            }
        }

        // Étape pour pré-télécharger les images nécessaires (gestion du timeout)
        stage('Pre-Pull Docker Images') {
            steps {
                script {
                    // Remplacez "myapp_image:latest" par le nom de votre image si elle est manquante
                    sh 'docker pull ibtihelgr/:latest || true'
                    // Vous pouvez ajouter ici d'autres images à pré-télécharger si besoin
                }
            }
        }

        // Étape pour déployer l'image Docker sur DockerHub
        stage('Deploy Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: '2775ddd2-4642-4423-abd2-2bea5948060f', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                            docker push ibtihelgr/tp-foyer:latest
                        '''
                    }
                }
            }
        }

        // Étape Docker Compose
        stage('Docker Compose') {
            steps {
                script {
                    echo "Running Docker Compose"
                    // Lancer les conteneurs en arrière-plan
                    sh 'docker compose up -d'
                }
            }
        }
    }
}
