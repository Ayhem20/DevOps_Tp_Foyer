pipeline {
    environment { 
        MAVEN_OPTS = '-Xms256m -Xmx512m'
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
                    credentialsId: 'GITbaha' // Ajoutez l'ID des identifiants ici
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
                echo "Building Docker Image"
                sh 'docker build -t tpfoyer-app:latest .'
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
                 post {
                            always {
                                emailext to: "baha.adouania@esprit.tn",
                                    subject: "jenkins build: ${currentBuild.currentResult}: ${env.JOB_NAME}",
                                    body: """
                                    Bonjour Baha Adouania,

                                    Le résultat de la dernière exécution du job Jenkins est : ${currentBuild.currentResult}

                                    Plus d'informations peuvent être trouvées ici : ${env.BUILD_URL}

                                    Cordialement,
                                    Jenkins
                                    """,
                                    from: "Baha Adouania <baha.adouania@esprit.tn>",
                                    attachLog: true
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
