pipeline {
    environment { 
        MAVEN_OPTS = '-Xms256m -Xmx512m'
        registry = "bahaa254/baha254"
        registryCredential = 'Dockerhub'
        dockerImageBackend = ''
    }

    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Code from Git') {
            steps {
                echo "Fetching Project from GitHub"
                git branch: 'baha',
                    url: 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git',
                    credentialsId: 'GITbaha'
            }
        }

        stage("Check JAVA and Maven Environment") {
            steps {
                echo "Checking JAVA_HOME and Maven version"
                sh 'echo $JAVA_HOME'
                sh 'java -version'
                sh 'mvn --version'
            }
        }

        stage('Build Backend Application') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Run JUnit and Mockito Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=sonar'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker Image"
                    dockerImageBackend = docker.build("${registry}:latest") // Construire l'image Docker
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', registryCredential) {
                        dockerImageBackend.push('latest') // Pousser l'image avec le tag 'latest'
                        dockerImageBackend.push("${env.BUILD_NUMBER}") // Pousser l'image avec le numéro de build comme tag
                    }
                }
            }
        }

        stage('Run Docker Compose') {
            steps {
                echo "Starting the application and MySQL with Docker Compose"
                sh 'docker-compose down'  // Arrêter les conteneurs précédents si nécessaires
                sh 'docker-compose up -d --build'  // Construire et démarrer les conteneurs
            }
        }

        stage('Deploy Backend to Nexus') {
            steps {
                sh 'mvn clean deploy -s /usr/share/maven/conf/settings.xml -DskipTests=true'
            }
        }
    }

    post {
        always {
            emailext(
                to: "baha.adouania@esprit.tn",
                subject: "Jenkins Build: ${currentBuild.currentResult}: ${env.JOB_NAME}",
                body: """
                Bonjour Baha Adouania,

                Le résultat de la dernière exécution du job Jenkins est : ${currentBuild.currentResult}

                Plus d'informations peuvent être trouvées ici : ${env.BUILD_URL}

                Cordialement,
                Jenkins
                """,
                from: "Baha Adouania <baha.adouania@esprit.tn>",
                attachLog: true
            )
        }
    }
}
