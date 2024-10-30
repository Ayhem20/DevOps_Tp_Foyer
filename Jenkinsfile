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
    }
}
